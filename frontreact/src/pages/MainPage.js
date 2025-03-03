import React, { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

const MainPage = () => {
    const { auth } = useContext(AuthContext);
    const isAuthenticated = !!auth.accessToken;

    return (
        <div style={{ padding: '20px' }}>
            <h2>메인 페이지</h2>
            {isAuthenticated ? (
                <p style={{ color: 'green' }}>
                    {auth.name} 님, 환영합니다!
                </p>
            ) : (
                <p>환영합니다! 로그인 후 더 많은 서비스를 이용해보세요.</p>
            )}
        </div>
    );
};

export default MainPage;
