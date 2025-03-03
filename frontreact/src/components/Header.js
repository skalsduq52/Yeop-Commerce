import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Header = () => {
    const { auth, logout } = useContext(AuthContext);
    const isAuthenticated = !!auth.accessToken;
    const navigate = useNavigate();

    const handleMyPage = () => {
        // 1. 서버로 로그아웃 요청
        //fetch('http://yeop-commerce.shop/user/info', {
        fetch('http://127.0.0.1/user/info', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.accessToken}`,
            }
        })
            .then(response => response.json())
            .then(data => {
                console.log('서버 응답 데이터:', data);
                navigate('/myPage', { state: { userInfo: data } });
            })
            .catch(error => console.error('마이페이지 에러:', error))
    };

    // 로그아웃 처리 함수
    const handleLogout = () => {
        // 1. 서버로 로그아웃 요청
        //fetch('http://yeop-commerce.shop/user/logout', {
        fetch('http://127.0.0.1/user/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.accessToken}`,
            }
        })
            .then(response => {
                if (response.ok) {
                    console.log('서버 로그아웃 성공');
                } else {
                    console.error('서버 로그아웃 실패');
                }
            })
            .catch(error => console.error('Logout Error:', error))
            .finally(() => {
                // 2. 프론트엔드에서 토큰 삭제 및 상태 초기화
                logout();
                navigate('/'); // 메인 페이지로 이동
            });
    };

    return (
        <header style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: '10px 20px',
            backgroundColor: '#282c34',
            color: 'white'
        }}>
            <div style={{ flex: 1, marginRight: '20px' }}>
                <input
                    type="text"
                    placeholder="검색..."
                    style={{
                        width: '100%',
                        padding: '8px 12px',
                        borderRadius: '4px',
                        border: '1px solid #ccc'
                    }}
                />
            </div>
            <div style={{ display: 'flex', gap: '10px' }}>
                {isAuthenticated ? (
                    <>
                        <button onClick={handleMyPage} style={{
                                padding: '8px 16px',
                                borderRadius: '4px',
                                border: 'none',
                                backgroundColor: '#61dafb',
                                cursor: 'pointer'
                            }}>
                                마이페이지
                        </button>
                        <button onClick={handleLogout} style={{
                            padding: '8px 16px',
                            borderRadius: '4px',
                            border: 'none',
                            backgroundColor: '#ff6b6b',
                            cursor: 'pointer'
                        }}>
                            로그아웃
                        </button>
                    </>
                ) : (
                    <>
                        <Link to="/login">
                            <button style={{
                                padding: '8px 16px',
                                borderRadius: '4px',
                                border: 'none',
                                backgroundColor: '#61dafb',
                                cursor: 'pointer'
                            }}>
                                로그인
                            </button>
                        </Link>
                        <Link to="/join">
                            <button style={{
                                padding: '8px 16px',
                                borderRadius: '4px',
                                border: 'none',
                                backgroundColor: '#61dafb',
                                cursor: 'pointer'
                            }}>
                                회원가입
                            </button>
                        </Link>
                    </>
                )}
            </div>
        </header>
    );
};

export default Header;
