import { createContext, useContext, useState } from 'react';

const Context = createContext();

export const ContextState = ({ children }) => {
    const [isIssue, setIsIssue] = useState(false);

    const updateValue = (newValue) => {
        setMyValue(newValue);
    };

    return (
        <Context.Provider value={{
            isIssue,
            setIsIssue
        }}>
            {children}
        </Context.Provider>
    );
};

export const useContextState = () => useContext(Context);
