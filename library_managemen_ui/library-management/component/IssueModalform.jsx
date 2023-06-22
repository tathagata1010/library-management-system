import { useState, useEffect } from "react";
import Modal from "react-modal";
import styles from "../styles/IssueModalForm.module.css";

const IssueBookModal = ({
  isOpen,
  onClose,
  onSubmit,
  formData,
  onChange,
  modalError,
  setModalError,
  isLoading,
}) => {
  const validatePhoneNumber = (phoneNumber) => {
    const phonePattern = /^[6-9]\d{9}$/;
    return phonePattern.test(phoneNumber);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const { borrowerPhone } = formData;
    if (!validatePhoneNumber(borrowerPhone)) {
      setModalError(
        <div className={styles.error}>
          Invalid phone number. Please enter a valid phone number.
        </div>
      );
    } else {
      onSubmit(e);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel="Issue Book"
      className={styles.modal}
    >
      <h2 className={styles.formHeading}>Issue Book</h2>
      <form onSubmit={handleSubmit} className={styles.form}>
        <label className={styles.label}>
          Issue Date
          <input
            type="date"
            name="issueDate"
            value={formData.issueDate}
            onChange={onChange}
            required
            max={new Date().toISOString().split("T")[0]}
          />
        </label>
        <label className={styles.label}>
          Borrower Name
          <input
            type="text"
            name="borrowerName"
            value={formData.borrowerName}
            onChange={onChange}
            required
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
        <button type="submit">
          {isLoading ? "Processing..." : "Issue Book"}
        </button>
      </form>
      {modalError && <p>{modalError}</p>}
    </Modal>
  );
};

export default IssueBookModal;
