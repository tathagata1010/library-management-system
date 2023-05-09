import { useEffect, useState } from "react";
import styles from "../styles/BookReportTable.module.css";
import { myAxios } from "../lib/create-axios";
import Modal from "react-modal";
import { Dropdown,Button } from "@nextui-org/react";

const BookReportTable = () => {
  const [bookReports, setBookReports] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedReport, setSelectedReport] = useState(null);
  const [formData, setFormData] = useState({
    returnDate: "",
  });

  useEffect(() => {
    myAxios
      .get("/borrowed")
      .then((response) => {
        console.log(response.data);
        setBookReports(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  const handleLost = (id) => {
    console.log(id);
    setBookReports((prevReports) =>
      prevReports.map((report) =>
        report.id === id
          ? { ...report, returnDate: null, isLost: true }
          : report
      )
    );
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

  const handleReturnSubmit = (e) => {
    e.preventDefault();
    const { returnDate } = formData;
    const isoDate = new Date(returnDate).toISOString();
    myAxios
      .put(`/borrowed/${selectedReport.id}`, { returnDate: isoDate })
      .then((response) => {
        setBookReports((prevReports) =>
          prevReports.map((report) =>
            report.id === selectedReport.id
              ? { ...report, returnDate, isLost: false }
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
              <td className={styles.tableData}>{bookReport.issueDate}</td>
              <td className={styles.tableData}>{bookReport.borrowerName}</td>
              <td className={styles.tableData}>{bookReport.borrowerPhone}</td>
              <td className={styles.tableData}>{bookReport.bookName}</td>
              <td className={styles.tableData}>
                {bookReport.isLost
                  ? "Lost"
                  : bookReport.returnDate == null
                  ? "Issued"
                  : "Returned"}
              </td>
              <td className={styles.tableData}>{bookReport.returnDate}</td>
              <td className={styles.actions}>
                {bookReport.returnDate == null ? (
                  <div>
                    <Dropdown>
                      <Dropdown.Button>Update Status</Dropdown.Button>
                      <Dropdown.Menu>
                        <Dropdown.Item childTag="button">
                          <button
                            onClick={() => {
                              setSelectedReport(bookReport);
                              setShowModal(true);
                            }}
                          >
                            Return
                          </button>
                        </Dropdown.Item>
                        <Dropdown.Item childTag="button">
                          <button onClick={() => handleLost(bookReport.id)}>
                            Lost
                          </button>
                        </Dropdown.Item>
                      </Dropdown.Menu>
                    </Dropdown>
                  </div>
                ) : (
                  <Dropdown>
                    <Dropdown.Button
                      disabled
                      className={styles.actionButtonDisabled}
                    >
                      Update Status
                    </Dropdown.Button>
                  </Dropdown>
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
              required
            />
          </div>
          <button type="submit">Submit</button>
        </form>
      </Modal>
    </div>
  );
};

export default BookReportTable;
