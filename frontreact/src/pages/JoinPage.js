import React, { useState,useContext  } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const JoinPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [phone, setPhone] = useState('');
    const [address, setAddress] = useState('');
    const [name, setName] = useState('');
    const [responseMessage, setResponseMessage] = useState('');
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        //fetch('http://yeop-commerce.shop/user/join', {
        fetch('http://127.0.0.1/user/join', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password, phone, address, name })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('회원가입 실패');
                }
                return response.json();
            })
            .then(data => {
                login(data);
                console.log(data);
                setResponseMessage('회원가입 성공!');
                navigate('/');

            })
            .catch(error => {
                setResponseMessage('회원가입 실패: ' + error.message);
            });
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>회원가입</h2>
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
                <div style={{ marginBottom: '10px' }}>
                    <label>전화번호: </label>
                    <input
                        type="tel"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        required
                        style={{ marginLeft: '10px' }}
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <label>주소: </label>
                    <input
                        type="text"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                        required
                        style={{ marginLeft: '10px' }}
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <label>이름: </label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
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
                    회원가입
                </button>
            </form>
            {responseMessage && <p style={{ marginTop: '10px' }}>{responseMessage}</p>}
        </div>
    );
};

export default JoinPage;
