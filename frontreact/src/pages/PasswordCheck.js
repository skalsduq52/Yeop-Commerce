import React, {useContext, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import {AuthContext} from "../context/AuthContext";

const PasswordCheck = () => {
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { auth } = useContext(AuthContext);

    const handleSubmit = (e) => {
        e.preventDefault();

        fetch('http://127.0.0.1/user/validatePassword', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.accessToken}`,
            },
            body: JSON.stringify({ password }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('비밀번호 검증 실패'); //
                }
                return response.json(); //
            })
            .then(data => {
                if (!data) {
                    throw new Error('비밀번호가 틀렸습니다.');
                }
                navigate('/myPage', { state: { userInfo: data, isEditing: true } });

            })
            .catch(error => {
                setError('서버 오류가 발생했습니다.');
            });
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>비밀번호 확인</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    비밀번호:
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </label>
                <button type="submit" style={{ marginLeft: '10px', padding: '8px 16px', backgroundColor: '#61dafb', color: 'white', border: 'none', cursor: 'pointer' }}>
                    확인
                </button>
            </form>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
};

export default PasswordCheck;
