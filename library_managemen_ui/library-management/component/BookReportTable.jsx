import { useEffect, useState } from "react";
import styles from "../styles/BookReportTable.module.css";
import { myAxios } from "../lib/create-axios";
import Modal from "react-modal";
const moment = require("moment");
import ButtonGroup from "react-bootstrap/ButtonGroup";
import { Dropdown } from "@nextui-org/react";


const BookReportTable = () => {
  const [bookReports, setBookReports] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedReport, setSelectedReport] = useState(null);
  const [selectedReportId, setSelectedReportId] = useState(null);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [returnDateSelect, setReturnDateSelect] = useState(Date);
  const [formData, setFormData] = useState({
    returnDate: "",
  });

  useEffect(() => {
    myAxios
      .get(`${process.env.NEXT_PUBLIC_ALL_BOOKS_RECORD_URI}`)
      .then((response) => {
        // console.log(response.data);
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
        console.log(typeof (returnDateSelect));
      }
    }
  }, [selectedReport, bookReports]);

  const handleLost = (id) => {
    console.log(id);
    const requestBody = {
      lost: true,
      returnDate: null,
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
        console.error(error);
      });
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

    setFormData({
      returnDate: "",
    });
  };

  const handleAction = (key, bookReport) => {
    if (key === "return") {
      setSelectedReport(bookReport);
      setShowModal(true);
    } else if (key === "lost") {
      handleLost(bookReport.id);
    }
  };

  const handleReturnSubmit = (e) => {
    e.preventDefault();
    const { returnDate } = formData;
    const isoDate = new Date(returnDate).toISOString();
    const ReturnURI = process.env.NEXT_PUBLIC_RETURN_LOST_URI.replace(
      "{bookid}",
      selectedReport.id
    ).replace("{borrowedid}", selectedReport.id);

    myAxios
      .put(ReturnURI, {
        returnDate: isoDate,
        isLost: false,
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
        console.log(error);
      });
  };

  return (
    <div>
      <table className={styles.table}>
        <thead>
          <tr>
            <th className={styles.tableHeader}>Issue Date</th>
            <th className={styles.tableHeader}>Borrower Wallet Address</th>
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
              <td className={styles.tableData}>{bookReport.borrowerAddress}</td>
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
      {/* {console.log(selectedReport)} */}
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
              min={returnDateSelect || undefined} // Set min attribute based on the issue date
              required
            />
          </div>
          <button className="buttonPrimary" type="submit">
            Returned
          </button>
        </form>
      </Modal>
    </div>
  );
};

export default BookReportTable;
