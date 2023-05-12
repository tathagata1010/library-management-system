import { useEffect, useState } from "react";
import styles from "../styles/BookTable.module.css";
import BookReportTable from "./BookReportTable";
import { useContextState } from "../context/ContextState";
import { myAxios } from "../lib/create-axios";
import Modal from "react-modal";
import IssueBookModal from "./IssueModalform";

const BookTable = () => {
  const { setIsIssue, isIssue } = useContextState();
  const [books, setBooks] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editBook, setEditBook] = useState(null);
  const [formData, setFormData] = useState({
    isbn: "",
    name: "",
    author: "",
    category: "",
  });
  const [issueFormData, setIssueFormData] = useState({
    issueDate: "",
    borrowerName: "",
    borrowerPhone: "",
    bookId: "",
  });
  useEffect(() => {
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
    setEditBook(book);
    console.log(book);
    setIssueFormData({
      issueDate: "",
      borrowerName: "",
      borrowerPhone: "",
      bookId: book.id,
    });
  };
  let index = 1;

  const closeIssueModal = () => {
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
      // search query is empty, fetch original list
      myAxios
        .get("/books")
        .then((response) => setBooks(response.data))
        .catch((error) => console.log(error));
    } else {
      // search query is not empty, fetch filtered list
      myAxios
        .get(`/books/name?name=${searchQuery}`)
        .then((response) => {
          if (Array.isArray(response.data)) {
            setBooks(response.data);
          } else {
            setBooks([response.data]);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };


  const handleCloseModal = () => {
    setShowModal(false);
    // reset the formData state when the modal is closed
    setFormData({
      isbn: "",
      name: "",
      author: "",
      category: "",
    });
    // reset the editBook state when the modal is closed
    setEditBook(null);
  };

  const handleView = async (book) => {
    try {
      const response = await myAxios.get(
        `/borrowed/books/${book.id}/borrowers`
      );
      const borrowers = response.data;
      // display borrowers list in modal or new page
      console.log(borrowers);
    } catch (error) {
      console.error(error);
    }
  };



  const handleIssueFormSubmit = (event) => {
    event.preventDefault();
    const issueBookData = {
      issueDate: issueFormData.issueDate,
      borrowerName: issueFormData.borrowerName,
      borrowerPhone: issueFormData.borrowerPhone,
      bookId: issueFormData.bookId,
    };

    myAxios
      .post("/borrowed", issueBookData)
      .then((response) => {
        setIsIssue(false);
      })
      .catch((error) => {
        console.log(error);
      });
    
  };

  const handleFormSubmit = (event) => {
    event.preventDefault();

    if (editBook) {
      const updatedBook = {
        ...editBook,
        isbn: formData.isbn,
        name: formData.name,
        author: formData.author,
        category: formData.category,
      };

      // update the book on the server
      myAxios
        .put(`books/${editBook.id}`, updatedBook)
        .then((response) => {
          // update the books state with the updated book
          setBooks(
            books.map((book) =>
              book.id === response.data.id ? response.data : book
            )
          );
          setShowModal(false);
        })
        .catch((error) => {
          console.log(error);
        });
    } else {
      const newBook = {
        isbn: formData.isbn,
        name: formData.name,
        author: formData.author,
        category: formData.category,
      };

      // add the new book to the server
      myAxios
        .post("/books", newBook)
        .then((response) => {
          // add the new book to the books state
          setBooks([...books, response.data]);
          setShowModal(false);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  const handleDelete = (book) => {
    // delete the book from the server
    myAxios
      .delete(`books/${book.id}`)
      .then((response) => {
        // remove the book from the books state
        setBooks(books.filter((b) => b.id !== book.id));
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <div>
      <div className={styles.buttonAndSearchContainer}>
        <button
          className={styles.actionButtonAdd}
          onClick={() => setShowModal(true)}
        >
          Add New Book
        </button>
        <div className={styles.searchBar}>
          <input
            type="text"
            placeholder="Search by book name"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyUp={handleSearch}
          />
        </div>
      </div>
      <table className={styles.table}>
        <thead>
          <tr>
            <th className={styles.tableHeader}>ID</th>
            <th className={styles.tableHeader}>ISBN</th>
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
              <td className={styles.tableData}>{book.isbn}</td>
              <td className={styles.tableData}>{book.name}</td>
              <td className={styles.tableData}>{book.author}</td>
              <td className={styles.tableData}>{book.category}</td>
              <td className={styles.actions}>
                {/* <button
                  className={styles.actionButton}
                  onClick={() => handleView(book)}
                >
                  View
                </button> */}
                <button
                  className={styles.actionButton}
                  onClick={() => handleEdit(book)}
                >
                  Edit
                </button>
                <button
                  className={styles.actionButton}
                  onClick={() => handleDelete(book)}
                >
                  Delete
                </button>
                <button
                  className={styles.actionButton}
                  onClick={() => handleIssue(book)}
                >
                  Issue
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {showModal && (
        <div>
          <Modal
            isOpen={showModal}
            onRequestClose={handleCloseModal}
            contentLabel="Add New Book Modal"
            className={styles.modal}
          >
            <h2 className={styles.formHeading}>
              {editBook ? "Update Book details" : "Add new Book"}
            </h2>
            <form onSubmit={handleFormSubmit} className={styles.form}>
              <label className={styles.label}>
                ISBN:
                <input
                  type="text"
                  name="isbn"
                  value={formData.isbn}
                  onChange={handleInputChange}
                  required
                />
              </label>
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
                {editBook ? "Update Book" : "Add Book"}
              </button>
            </form>
          </Modal>
        </div>
      )}
      ;
      {isIssue && (
        <div>
          <IssueBookModal
            isOpen={isIssue}
            onClose={closeIssueModal}
            onSubmit={handleIssueFormSubmit}
            formData={issueFormData}
            onChange={handleIssueInputChange}
          />
        </div>
      )}
      ;
    </div>
  );
};

export default BookTable;
