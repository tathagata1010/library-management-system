import React from "react";
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
}) => {
  const validatePhoneNumber = (phoneNumber) => {
    // Regular expression pattern for Indian phone number (10 digits)
    const phonePattern = /^[6-9]\d{9}$/;
    return phonePattern.test(phoneNumber);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const { borrowerPhone } = formData;
    if (!validatePhoneNumber(borrowerPhone)) {
      // Display error message if phone number is invalid
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
        
        {/* <label className={styles.label}>
          Borrower Wallet Address:
          <input
            type="text"
            name="walletAddress"
            onChange={onChange}
            required
            value={formData.walletAddress}
          />
        </label> */}
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
        <button type="submit">Issue Book</button>
      </form>
      {modalError && <p>{modalError}</p>}
    </Modal>
  );
};

export default IssueBookModal;
