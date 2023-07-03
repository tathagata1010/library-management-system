import { useEffect, useState } from "react";
import styles from "../styles/BookReportTable.module.css";
import { myAxios } from "../lib/create-axios";
import Modal from "react-modal";
import {
  useAccount,
  useConnect,
  useSignMessage,
  useDisconnect,
  sepolia,
} from "wagmi";
import { WalletConnectConnector } from "wagmi/connectors/walletConnect";
const moment = require("moment");

const BookReportTable = () => {
  const [bookReports, setBookReports] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [lostModalOpen, setLostModalOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const [selectedReport, setSelectedReport] = useState(null);
  const [selectedReportId, setSelectedReportId] = useState(null);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [returnDateSelect, setReturnDateSelect] = useState(Date);
  const [modalContent, setModalContent] = useState(null);
  const [formData, setFormData] = useState({ returnDate: "" });
  const { connectAsync } = useConnect();
  const { disconnectAsync } = useDisconnect();
  const { address, isConnected } = useAccount();

  const { data, signMessageAsync } = useSignMessage();

  useEffect(() => {
   
    myAxios
      .get(`${process.env.NEXT_PUBLIC_ALL_BOOKS_RECORD_URI}`)
      .then((response) => {
        setBookReports(response.data);
      })
      .catch((error) => {
        console.log(error);
      });

    if (selectedReport) {
      const selectedBook = bookReports.find(
        (bookReport) => bookReport.id === selectedReport.id
      );
      if (selectedBook) {
        setReturnDateSelect(selectedBook.issueDate);
      }
    }
  }, [selectedReport, bookReports]);

  const handleLost = async (id) => {
    console.log(id);
    console.log(isLoading);
    if (isLoading) return;

    setIsLoading(true);

    try {
      if (isConnected) {
        console.log(isConnected);
        await disconnectAsync();
      }

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
      const borrowerMessage = "Sign as borrower to report the book lost";
      const data = await signMessageAsync({ message: borrowerMessage });

      console.log("Borrower signature:  ", data);
      console.log("Borrower Address:", borrowerAddress);
      await disconnectAsync();

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
      const librarianMessage = "Sign as librarian to mark the book lost";
      const librarianSignature = await ethereum.request({
        method: "personal_sign",
        params: [librarianMessage, librarianAddress],
      });
      console.log("Librarian Signature:", librarianSignature);
      console.log("Librarian Address:", librarianAddress);

      const requestBody = {
        lost: true,
        returnDate: null,
        borrowerAddress: borrowerAddress,
      };
      const LostURI = process.env.NEXT_PUBLIC_RETURN_LOST_URI.replace(
        "{bookid}",
        id
      ).replace("{borrowedid}", id);
      myAxios
        .put(LostURI, requestBody)
        .then((response) => {
          setBookReports((prevReports) =>
            prevReports.map((report) =>
              report.id === response.data.id ? response.data : report
            )
          );
        })
        .catch((error) => {
          setLostModalOpen(true);
          setErrorMessage(error.response.data.errorCode);
        })
        .finally(() => {
          setIsLoading(false);
        });
    } catch (error) {
      console.error(error);
    }
  };

  const toggleDropdown = (reportId) => {
    setDropdownOpen(!dropdownOpen);
    setSelectedReportId((prevId) => (prevId === reportId ? null : reportId));
  };

  const onChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setModalContent(null);
    setFormData({ returnDate: "" });
  };

  const handleReturnSubmit = async (e) => {
    e.preventDefault();
    if (isLoading) return;
    setIsLoading(true);
    setModalContent(null);
    const { returnDate } = formData;
    const isoDate = new Date(returnDate).toISOString();
    const ReturnURI = process.env.NEXT_PUBLIC_RETURN_LOST_URI.replace(
      "{bookid}",
      selectedReport.id
    ).replace("{borrowedid}", selectedReport.id);

    try {
      if (isConnected) {
        console.log(isConnected);
        await disconnectAsync();
      }

      const { account } = await connectAsync({
        connector: new WalletConnectConnector({
          chains: [sepolia],
          options: {
            qrcode: true,
            projectId: "148d4205eff1740aa9f73b236d70ca2d",
          },
        }),
      });

      const borrowerAddress = account; // Get the first account from the borrowerAccounts array
      const borrowerMessage = "Sign as borrower to return the book";
      const data = await signMessageAsync({ message: borrowerMessage });
      console.log("Borrower signature:  ", data);
      console.log("Borrower Address:", borrowerAddress);
      await disconnectAsync();

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
      const librarianMessage = "Sign as librarian to mark the book return";
      const librarianSignature = await ethereum.request({
        method: "personal_sign",
        params: [librarianMessage, librarianAddress],
      });
      console.log("Librarian Signature:", librarianSignature);
      console.log("Librarian Address:", librarianAddress);

      myAxios
        .put(ReturnURI, {
          returnDate: isoDate,
          isLost: false,
          borrowerAddress: borrowerAddress,
        })
        .then((response) => {
          setBookReports((prevReports) =>
            prevReports.map((report) =>
              report.id === selectedReport.id
                ? { ...report, returnDate, lost: false }
                : report
            )
          );
          setShowModal(false);
          setSelectedReport(null);
        })
        .catch((error) => {
          const errorCode = error.response.data.errorCode;
          setModalContent(errorCode);
        })
        .finally(() => {
          setIsLoading(false);
        });
    } catch (error) {
      setShowModal(false);
      console.error(error);
    }
  };

  const handleLostModalOpen = () => {
    setLostModalOpen(true);
  };

  const handleLostModalClose = () => {
    setLostModalOpen(false);
  };

  return (
    <div>
      <table className={styles.table}>
        <thead>
          <tr>
            <th className={styles.tableHeader}>Issue Date</th>
            <th className={styles.tableHeader}>Borrower Wallet Address</th>
            <th className={styles.tableHeader}>Borrower Name</th>
            <th className={styles.tableHeader}>Borrower Phone Number</th>
            <th className={styles.tableHeader}>Book Name</th>
            <th className={styles.tableHeader}>Status</th>
            <th className={styles.tableHeader}>Return Date</th>
            <th className={styles.tableHeader}>Action</th>
          </tr>
        </thead>
        <tbody>
          {bookReports.map((bookReport) => (
            <tr key={bookReport.id}>
              <td className={styles.tableData}>
                {moment(bookReport.issueDate).format("MM/DD/YYYY")}
              </td>
              <td
                className={styles.tableData}
              >{`${bookReport.borrowerAddress.slice(
                0,
                6
              )}..${bookReport.borrowerAddress.slice(-4)}`}</td>{" "}
              <td className={styles.tableData}>{bookReport.borrowerName}</td>
              <td className={styles.tableData}>{bookReport.borrowerPhone}</td>
              <td className={styles.tableData}>{bookReport.bookName}</td>
              <td className={styles.tableData}>
                {bookReport.lost
                  ? "Lost"
                  : bookReport.returnDate == null
                  ? "Issued"
                  : "Returned"}
              </td>
              <td className={styles.tableData}>
                {bookReport.returnDate
                  ? moment(bookReport.returnDate).format("MM/DD/YYYY")
                  : null}
              </td>
              <td className={styles.actions}>
                {bookReport.returnDate == null && bookReport.lost === false ? (
                  <div>
                    <div
                      className={`${styles.dropdown} ${
                        dropdownOpen ? styles.open : ""
                      }`}
                    >
                      <button
                        className={`${styles.dropbtn} ${
                          selectedReportId === bookReport.id ? styles.open : ""
                        }`}
                        onClick={() => toggleDropdown(bookReport.id)}
                      >
                        Update Status
                      </button>
                      {selectedReportId === bookReport.id && dropdownOpen && (
                        <div class={styles.dropdownContent}>
                          <button
                            class={styles.dropdownButton}
                            onClick={() => {
                              setSelectedReport(bookReport);
                              setShowModal(true);
                              toggleDropdown(bookReport.id);
                            }}
                          >
                            Return
                          </button>
                          <button
                            class={styles.dropdownButton}
                            onClick={() => {
                              handleLost(bookReport.id);
                              toggleDropdown(bookReport.id);
                            }}
                          >
                            Lost
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                ) : (
                  <div class={styles.dropdown}>
                    <button class={styles.buttonDisabled}>Update Status</button>
                  </div>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <Modal
        isOpen={showModal}
        onRequestClose={handleCloseModal}
        contentLabel="Return Book Modal"
        className={styles.modal}
      >
        <h2>Return Book</h2>
        <form className={styles.form} onSubmit={handleReturnSubmit}>
          <div>
            <label className={styles.label} htmlFor="returnDate">
              Return Date
            </label>
            <input
              type="date"
              name="returnDate"
              value={formData.returnDate}
              onChange={onChange}
              min={returnDateSelect || undefined}
              required
            />
          </div>
          <button className="buttonPrimary" type="submit">
            {isLoading ? "Processing..." : "Return"}
          </button>
        </form>
        {modalContent && <p className={styles.error}> {modalContent}</p>}
      </Modal>

      <Modal
        isOpen={lostModalOpen}
        onRequestClose={handleLostModalClose}
        contentLabel="Delete Book"
        className={styles.modal}
      >
        <h2>Report Book Lost Error</h2>
        <div>
          <p>Lost operation failed as it, {errorMessage} !</p>
          <button onClick={handleLostModalClose} className="buttonPrimary">
            Ok
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default BookReportTable;
