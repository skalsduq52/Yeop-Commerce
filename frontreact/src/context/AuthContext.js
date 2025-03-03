import React, { createContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

// AuthContext 생성
export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [auth, setAuth] = useState({
        accessToken: null,
        email: null,
        name: null,
    });
    const navigate = useNavigate();

    // LocalStorage에 저장 및 가져오기
    useEffect(() => {
        const storedAuth = JSON.parse(localStorage.getItem('auth'));
        if (storedAuth) {
            setAuth(storedAuth);
        }
    }, []);

    useEffect(() => {
        localStorage.setItem('auth', JSON.stringify(auth));
    }, [auth]);

    // 로그인
    const login = (data) => {
        setAuth(data);
        localStorage.setItem('auth', JSON.stringify(data));
    };

    // 로그아웃
    const logout = () => {
        setAuth({ accessToken: null, email: null, name: null });
        localStorage.removeItem('auth');
        navigate('/');
    };

    return (
        <AuthContext.Provider value={{ auth, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};
