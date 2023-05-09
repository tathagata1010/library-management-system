import React from "react";
import Modal from "react-modal";
import styles from "../styles/IssueModalForm.module.css";

const IssueBookModal = ({ isOpen, onClose, onSubmit, formData, onChange }) => {
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel="Issue Book"
      className={styles.modal}
    >
      <h2 className={styles.formHeading}>Issue Book Modal</h2>
      <form onSubmit={onSubmit} className={styles.form}>
        <label className={styles.label}>
          Issue Date
          <input
            type="date"
            name="issueDate"
            value={formData.issueDate}
            onChange={onChange}
            required
          />
        </label>
        <label className={styles.label}>
          Book Id:
          <input
            type="text"
            name="bookId"
            onChange={onChange}
            required
            value={formData.bookId}
          />
        </label>
        <label className={styles.label}>
          Borrower Name:
          <input
            type="text"
            name="borrowerName"
            onChange={onChange}
            required
            value={formData.borrowerName}
          />
        </label>
        <label className={styles.label}>
          Borrower Phone Number:
          <input
            type="text"
            name="borrowerPhone"
            onChange={onChange}
            required
            value={formData.borrowerPhone}
          />
        </label>
        <button type="submit">Issue book</button>
      </form>
    </Modal>
  );
};

export default IssueBookModal;
