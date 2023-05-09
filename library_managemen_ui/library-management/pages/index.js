import styles from '../styles/Home.module.css';
import BookTable from '../component/BookTable';
import BookReportTable from '../component/BookReportTable';
import { useContextState } from '../context/ContextState';
import { useState } from 'react';

export default function Home() {
  const { isIssue } = useContextState();
  const [showBookTable, setShowBookTable] = useState(true);

  return (
    <div>
      <nav className={styles.navbar}>
        <ul className={styles.navList}>
          <li>
            <button
              className={`${styles.navButton} ${showBookTable ? styles.active : ''}`}
              onClick={() => setShowBookTable(true)}
            >
              Home
            </button>
          </li>
          <li>
            <button
              className={`${styles.navButton} ${!showBookTable ? styles.active : ''}`}
              onClick={() => setShowBookTable(false)}
            >
              Borrowed Records
            </button>
          </li>
        </ul>
        <h1 className={styles.navHeading}>Library Management</h1>
      </nav>
      {showBookTable ? <BookTable /> : <BookReportTable />}
    </div>
  );
}
