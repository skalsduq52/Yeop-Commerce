import React, {useContext, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [responseMessage, setResponseMessage] = useState('');
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        //fetch('http://yeop-commerce.shop/user/login', {
        fetch('http://127.0.0.1/user/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('로그인 실패');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                login(data);
                setResponseMessage('로그인 성공!');
                navigate('/');
            })
            .catch(error => {
                setResponseMessage('로그인 실패: ' + error.message);
            });
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>로그인</h2>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '10px' }}>
                    <label>이메일: </label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        style={{ marginLeft: '10px' }}
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <label>비밀번호: </label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        style={{ marginLeft: '10px' }}
                    />
                </div>
                <button type="submit" style={{
                    padding: '8px 16px',
                    borderRadius: '4px',
                    border: 'none',
                    backgroundColor: '#61dafb',
                    cursor: 'pointer'
                }}>
                    로그인
                </button>
            </form>
            {responseMessage && <p style={{ marginTop: '10px' }}>{responseMessage}</p>}
        </div>
    );
};

export default LoginPage;
