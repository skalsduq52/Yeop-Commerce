import React, { useContext, useState, useEffect } from 'react';
import { AuthContext } from '../context/AuthContext';
import { useLocation, useNavigate  } from 'react-router-dom';

const MyPage = () => {

    const location = useLocation();
    const navigate = useNavigate();
    const userInfo = location.state?.userInfo;
    const { logout } = useContext(AuthContext);
    const { auth } = useContext(AuthContext);

    const isEditingFromState = location.state?.isEditing || false;
    const [isEditing, setIsEditing] = useState(isEditingFromState);

    const [userData, setUserData] = useState(userInfo || {});

    useEffect(() => {
        if (!userInfo) {
            navigate('/password-check'); // 비밀번호 확인 페이지로 리디렉션
        }
        console.log(isEditing);
    }, [userInfo, navigate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData({ ...userData, [name]: value });
    };

    const handleSave = () => {
        fetch('http://127.0.0.1/user/update', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.accessToken}`,
            },
            body: JSON.stringify(userData),
        })
            .then(response => response.json())
            .then(data => {
                console.log('업데이트 성공:', data);
                setIsEditing(false);
            })
            .catch(error => console.error('업데이트 실패:', error));
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>마이페이지</h2>

            {isEditing ? (
                <>
                    <p><strong>이메일:</strong> {userData.email}</p>
                    <p><strong>이름:</strong> {userData.name}</p>
                    <p>
                        <strong>주소:</strong>
                        <input type="text" name="address" value={userData.address} onChange={handleChange} />
                    </p>
                    <p>
                        <strong>전화번호:</strong>
                        <input type="text" name="phone" value={userData.phone} onChange={handleChange} />
                    </p>
                    <button onClick={handleSave} style={{ backgroundColor: '#4CAF50', color: 'white', padding: '8px 16px', borderRadius: '4px', border: 'none', cursor: 'pointer' }}>
                        저장
                    </button>
                </>
            ) : (
                <>
                    <p><strong>이메일:</strong> {userData.email}</p>
                    <p><strong>이름:</strong> {userData.name}</p>
                    <p><strong>주소:</strong> {userData.address}</p>
                    <p><strong>전화번호:</strong> {userData.phone}</p>
                    <button onClick={() => navigate('/password-check')} style={{ backgroundColor: '#61dafb', color: 'white', padding: '8px 16px', borderRadius: '4px', border: 'none', cursor: 'pointer', marginRight: '10px' }}>
                        수정
                    </button>
                    <button onClick={logout} style={{ backgroundColor: '#ff6b6b', color: 'white', padding: '8px 16px', borderRadius: '4px', border: 'none', cursor: 'pointer' }}>
                        로그아웃
                    </button>
                </>
            )}
        </div>
    );
};

export default MyPage;
