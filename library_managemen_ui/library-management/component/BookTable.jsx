import { useEffect, useState } from "react";
import styles from "../styles/BookTable.module.css";
import BookReportTable from "./BookReportTable";
import {
  useAccount,
  useConnect,
  useSignMessage,
  useDisconnect,
  sepolia,
} from "wagmi";
import { WalletConnectConnector } from "wagmi/connectors/walletConnect";
import { useContextState } from "../context/ContextState";
import { myAxios } from "../lib/create-axios";
import Modal from "react-modal";
import IssueBookModal from "./IssueModalform";

const BookTable = () => {
  const { setIsIssue, isIssue } = useContextState();
  const [books, setBooks] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [modalContent, setModalContent] = useState(null);
  const [editBook, setEditBook] = useState(null);
  const [searchError, setSearchError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [deleteError, setDeleteError] = useState(null);
  const [deleteBookId, setDeleteBookId] = useState(null);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [deleteBook, setDeleteBook] = useState(null);
  const { connectAsync } = useConnect();
  const { disconnectAsync } = useDisconnect();
  const { address, isConnected } = useAccount();

  const { data, signMessageAsync } = useSignMessage();

  const [formData, setFormData] = useState({
    isbn: "",
    name: "",
    author: "",
    category: "",
  });
  const [issueFormData, setIssueFormData] = useState({
    issueDate: "",
    borrowerName: "",
    walletAddress: "",
    borrowerPhone: "",
    bookId: "",
  });
  useEffect(() => {
    disconnectAsync();
    setIsLoading(false);
    myAxios
      .get("/books")
      .then((response) => {
        console.log(response);
        setBooks(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  const [searchQuery, setSearchQuery] = useState("");

  const handleIssue = (book) => {
    setIsIssue(true);
    setModalContent(null);
    setEditBook(book);
    console.log(book);
    setIssueFormData({
      issueDate: "",
      borrowerName: "",
      walletAddress: "",
      borrowerPhone: "",
      bookId: book.id,
    });
  };
  let index = 1;

  const closeIssueModal = () => {
    setModalContent(null);
    setIsIssue(false);
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleIssueInputChange = (event) => {
    const { name, value } = event.target;
    setIssueFormData({
      ...issueFormData,
      [name]: value,
    });
  };

  const handleEdit = (book) => {
    setEditBook(book);
    setShowModal(true);
    setFormData({
      isbn: book.isbn,
      name: book.name,
      author: book.author,
      category: book.category,
    });
  };

  const handleSearch = () => {
    if (searchQuery.trim() === "") {
      myAxios
        .get(`${process.env.NEXT_PUBLIC_ALL_BOOKS_URI}`)
        .then((response) => setBooks(response.data))
        .catch((error) => console.log(error));
      setSearchError(null);
    } else {
      myAxios
        .get(`${process.env.NEXT_PUBLIC_BOOK_SEARCH_URI}${searchQuery}`)
        .then((response) => {
          if (Array.isArray(response.data)) {
            setBooks(response.data);
            setSearchError(null);
          } else {
            setSearchError(null);
            setBooks([response.data]);
          }
        })
        .catch((error) => {
          setSearchError(error.response.data.errorMessage);
        });
    }
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setModalContent(null);
    setFormData({
      isbn: "",
      name: "",
      author: "",
      category: "",
    });
    setEditBook(null);
  };

  const handleIssueFormSubmit = async (event) => {
    event.preventDefault();
    await disconnectAsync();
    if (isLoading) return;
    setIsLoading(true);
    try {
      if (isConnected) {
        console.log(isConnected);
        await disconnectAsync();
      }

      const selectedLibrarianAccount = await ethereum.request({
        method: "wallet_requestPermissions",
        params: [
          {
            eth_accounts: {},
          },
        ],
      });
      const librarianAccounts = await ethereum.request({
        method: "eth_requestAccounts",
      });
      const librarianAddress = librarianAccounts[0];
      const librarianMessage = "Sign as librarian to issue the book";
      const librarianSignature = await ethereum.request({
        method: "personal_sign",
        params: [librarianMessage, librarianAddress],
      });
      console.log("Librarian Signature:", librarianSignature);
      console.log("Librarian Address:", librarianAddress);

      const { account } = await connectAsync({
        connector: new WalletConnectConnector({
          chains: [sepolia],
          options: {
            qrcode: true,
            projectId: "148d4205eff1740aa9f73b236d70ca2d",
          },
        }),
      });
      const borrowerAddress = account;
      const borrowerMessage = "Sign as borrower to borrow the book";

      const data = await signMessageAsync({ message: borrowerMessage });

      console.log("Borrower Signature:", data);

      await disconnectAsync();

      console.log("Borrower Address:", borrowerAddress);

      const issueBookData = {
        issueDate: issueFormData.issueDate,
        borrowerName: issueFormData.borrowerName,
        walletAddress: borrowerAddress,
        borrowerPhone: issueFormData.borrowerPhone,
        bookId: issueFormData.bookId,
      };

      const issueBookURI = process.env.NEXT_PUBLIC_BOOK_ISSUE_URI.replace(
        "{issueBookData.bookId}",
        issueBookData.bookId
      );

      myAxios
        .post(issueBookURI, issueBookData)
        .then((response) => {
          setIsIssue(false);
        })
        .catch((error) => {
          const errorCode = error.response.data.errorCode;
          const errorMessage = error.response.data.errorMessage;
          setModalContent(
            <div className={styles.error}>
              <p>
                {errorCode}: {errorMessage}
              </p>
            </div>
          );
        })
        .finally(() => {
          setIsLoading(false);
        });
    } catch (error) {
      console.error(error);
    }
  };

  const handleFormSubmit = (event) => {
    event.preventDefault();
    if (isLoading) return;
    setIsLoading(true);
    setModalContent(null);
    if (editBook) {
      const updatedBook = {
        ...editBook,
        name: formData.name,
        author: formData.author,
        category: formData.category,
      };

      // update the book on the server
      const editBookURI = process.env.NEXT_PUBLIC_EDIT_BOOK_URI.replace(
        "{editBook.id}",
        editBook.id
      );

      myAxios
        .put(editBookURI, updatedBook)
        .then((response) => {
          setBooks(
            books.map((book) =>
              book.id === response.data.id ? response.data : book
            )
          );
          setShowModal(false);
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => setIsLoading(false));
    } else {
      const newBook = {
        name: formData.name,
        author: formData.author,
        category: formData.category,
      };

      // add the new book to the server
      myAxios
        .post(`${process.env.NEXT_PUBLIC_ALL_BOOKS_URI}`, newBook)
        .then((response) => {
          // add the new book to the books state
          setBooks([...books, response.data]);
          setShowModal(false);
        })
        .catch((error) => {
          const errorCode = error.response.data.errorCode;
          const errorMessage = error.response.data.errorMessage;
          setModalContent(
            <div className={styles.error}>
              <p>
                {errorCode}: {errorMessage}
              </p>
            </div>
          );
        })
        .finally(() => setIsLoading(false));
    }
  };

  const handleDelete = (book) => {
    setDeleteBook(book);
    setDeleteModalOpen(true);
    setDeleteError(null);
  };

  const handleDeleteConfirm = () => {
    if (deleteBook) {
      if (isLoading) return;
      setIsLoading(true);
      const deleteBookURI = process.env.NEXT_PUBLIC_DELETE_BOOK_URI.replace(
        "{book.id}",
        deleteBook.id
      );

      myAxios
        .delete(deleteBookURI)
        .then((response) => {
          // remove the book from the books state
          setBooks(books.filter((b) => b.id !== deleteBook.id));
          setDeleteError(null);
          setDeleteModalOpen(false); // Close the delete modal
        })
        .catch((error) => {
          console.log(error);
          setDeleteError(error.response.data.errorMessage);
        })
        .finally(() => {
          setIsLoading(false);
        });
    }
  };

  const handleDeleteClose = () => {
    setDeleteBook(null);
    setDeleteModalOpen(false);
    setDeleteError(null);
  };

  return (
    <div>
      <div className={styles.buttonAndSearchContainer}>
        <button
          className="buttonPrimary buttonMargin"
          onClick={() => setShowModal(true)}
        >
          Add New Book
        </button>
        <div className={styles.searchBarContainer}>
          <div className={styles.searchBar}>
            <input
              type="text"
              placeholder="Search by book name"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <button onClick={handleSearch} className={styles.buttonPrimary}>
              Search
            </button>
          </div>
          {searchError && <p className={styles.error}>{searchError}</p>}
        </div>
      </div>
      <table className={styles.table}>
        <thead>
          <tr>
            <th className={styles.tableHeader}>ID</th>
            <th className={styles.tableHeader}>Name</th>
            <th className={styles.tableHeader}>Author</th>
            <th className={styles.tableHeader}>Category</th>
            <th className={styles.tableHeader}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {books.map((book) => (
            <tr key={book.id}>
              <td className={styles.tableData}>{index++}</td>
              <td className={styles.tableData}>{book.name}</td>
              <td className={styles.tableData}>{book.author}</td>
              <td className={styles.tableData}>{book.category}</td>
              <td className={styles.actions}>
                <button
                  className="buttonPrimary button-dimension"
                  onClick={() => handleIssue(book)}
                >
                  Issue
                </button>
                <button
                  className="buttonSecondary button-dimension"
                  onClick={() => handleEdit(book)}
                >
                  Edit
                </button>
                <button
                  className="buttonTertiary button-dimension"
                  onClick={() => handleDelete(book)}
                >
                  Delete
                </button>
                {deleteError && deleteBookId === book.id && (
                  <p className={styles.error}>Error: {deleteError}</p>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {deleteBook && (
        <Modal
          isOpen={deleteModalOpen}
          onRequestClose={handleDeleteClose}
          contentLabel="Delete Book"
          className={styles.modal}
        >
          <h2>Delete Book</h2>

          {deleteError ? (
            <div>
              <p>Delete operation failed because {deleteError} !</p>
              <button onClick={handleDeleteClose} className="buttonPrimary">
                Ok
              </button>
            </div>
          ) : (
            <div>
              <p>Are you sure you want to delete this book?</p>
              <button onClick={handleDeleteConfirm} className="buttonTertiary">
                {isLoading ? "Deleting..." : "Delete"}
              </button>
              <button onClick={handleDeleteClose} className="buttonPrimary">
                Close
              </button>
            </div>
          )}
        </Modal>
      )}
      {showModal && (
        <div>
          <Modal
            isOpen={showModal}
            onRequestClose={handleCloseModal}
            contentLabel="Add New Book"
            className={styles.modal}
          >
            <h2 className={styles.formHeading}>
              {editBook ? "Update Book Details" : "Add New Book"}
            </h2>
            <form onSubmit={handleFormSubmit} className={styles.form}>
              <label className={styles.label}>
                Name:
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  required
                />
              </label>
              <label className={styles.label}>
                Author:
                <input
                  type="text"
                  name="author"
                  value={formData.author}
                  onChange={handleInputChange}
                  required
                />
              </label>
              <label className={styles.label}>
                Category:
                <input
                  type="text"
                  name="category"
                  value={formData.category}
                  onChange={handleInputChange}
                  required
                />
              </label>
              <button type="submit">
                {isLoading
                  ? "Processing..."
                  : editBook
                  ? "Update Book"
                  : "Add Book"}
              </button>
            </form>
            {modalContent && <p>{modalContent}</p>}
          </Modal>
        </div>
      )}
      ;
      {isIssue && (
        <div>
          <IssueBookModal
            isOpen={isIssue}
            isLoading={isLoading}
            onClose={closeIssueModal}
            onSubmit={handleIssueFormSubmit}
            formData={issueFormData}
            onChange={handleIssueInputChange}
            modalError={modalContent}
            setModalError={setModalContent}
          />
        </div>
      )}
      ;
    </div>
  );
};

export default BookTable;
