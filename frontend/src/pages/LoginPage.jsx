// React와 react-router-dom에서 필요한 기능들을 가져옵니다.
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
// api 호출을 위한 모듈을 가져옵니다.
import api from '../api';

// 컴포넌트에 사용될 스타일을 정의합니다.
const styles = {
    pageContainer: { display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', backgroundColor: '#f0f2f5', fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif' },
    box: { width: '90%', maxWidth: '400px', padding: '40px 30px', backgroundColor: 'white', borderRadius: '16px', boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)', textAlign: 'center' },
    headerTitle: { color: '#FF7F50', fontSize: '32px', fontWeight: 'bold', marginBottom: '30px', letterSpacing: '1px' },
    boxTitle: { fontSize: '24px', fontWeight: 'bold', marginBottom: '24px' },
    welcomeMessage: { fontSize: '18px', fontWeight: '500', marginBottom: '30px', lineHeight: '1.5', wordBreak: 'break-all', color: '#333' },
    form: { display: 'flex', flexDirection: 'column', gap: '16px' },
    input: { width: '100%', padding: '12px 16px', borderRadius: '8px', border: '1px solid #ddd', fontSize: '16px', boxSizing: 'border-box' },
    button: { width: '100%', padding: '12px', borderRadius: '8px', border: 'none', fontSize: '16px', fontWeight: 'bold', cursor: 'pointer', transition: 'background-color 0.2s' },
    primaryButton: { backgroundColor: '#007bff', color: 'white' },
    divider: { display: 'flex', alignItems: 'center', textAlign: 'center', color: '#888', margin: '24px 0' },
    dividerLine: { flex: 1, height: '1px', backgroundColor: '#ddd' },
    dividerText: { margin: '0 10px' },
    googleButton: { backgroundColor: '#4285F4', color: 'white', marginBottom: '12px' },
    naverButton: { backgroundColor: '#03C75A', color: 'white' },
    errorMessage: { color: '#dc3545', marginTop: '16px', fontSize: '14px' }
};

// 로그인 페이지 컴포넌트입니다.
function LoginPage() {
    // 페이지 이동을 위한 navigate 함수를 사용합니다.
    const navigate = useNavigate();
    // 컴포넌트의 상태를 관리하는 변수들입니다.
    const [user, setUser] = useState(null); // 현재 로그인한 사용자 정보
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [email, setEmail] = useState(''); // 이메일 입력값
    const [password, setPassword] = useState(''); // 비밀번호 입력값
    const [error, setError] = useState(''); // 에러 메시지

    // 컴포넌트가 렌더링될 때 로그인 상태를 확인합니다.
    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                // 서버에 사용자 정보를 요청하여 이미 로그인되어 있는지 확인합니다.
                await api.get('/api/users/me');
                // 로그인 되어 있다면 장소 목록 페이지로 이동합니다.
                navigate('/places');
                setUser(Response.data);
            } catch (err) {
                // 로그인 되어 있지 않으면 사용자 정보를 null로 설정합니다.
                setUser(null);
            } finally {
                setLoading(false);
            }
        };
        checkLoginStatus();
    }, [navigate]);

    // 이메일 로그인 폼 제출 시 실행되는 함수입니다.
    const handleEmailLogin = async (e) => {
        e.preventDefault(); // 폼의 기본 동작(페이지 새로고침)을 막습니다.
        setError('');
        try {
            // 서버에 이메일과 비밀번호로 로그인을 요청합니다.
            await api.post('/api/auth/login', { email, password });
            // 로그인 성공 시 장소 목록 페이지로 이동합니다.
            navigate('/places');
        } catch (err) {
            setError('이메일 또는 비밀번호가 올바르지 않습니다.');
            console.error('Login failed:', err);
        }
    };

    // 소셜 로그인 버튼 클릭 시 실행되는 함수입니다.
    const handleSocialLogin = (provider) => {
        // 해당 소셜 로그인 페이지로 이동합니다.
        window.location.href = `/oauth2/authorization/${provider}`;
    };

    // 로그아웃 버튼 클릭 시 실행되는 함수입니다.
    const handleLogout = async () => {
        try {
            // 서버에 로그아웃을 요청합니다.
            await api.post('/api/auth/logout');
            setUser(null);
            window.location.reload(); // 페이지를 새로고침하여 로그인 상태를 반영합니다.
        } catch (err) {
            console.error('Logout failed:', err);
        }
    };

    // 로딩 중일 때 보여줄 화면입니다.
    if (loading) {
        return <div style={styles.pageContainer}><div>로딩 중...</div></div>;
    }

    // 화면에 보여질 JSX를 반환합니다.
    return (
        <div style={styles.pageContainer}>
            <div style={styles.box}>
                <h1 style={styles.headerTitle}>JeJu Trip</h1>
                {/* 사용자가 로그인한 경우 환영 메시지와 로그아웃 버튼을 보여줍니다. */}
                {user ? (
                    <div>
                        <p style={styles.welcomeMessage}>{`${user}님 환영합니다.`}</p>
                        <button
                            onClick={handleLogout}
                            style={{ ...styles.button, ...styles.primaryButton }}
                        >
                            로그아웃
                        </button>
                    </div>
                ) : (
                    /* 로그인하지 않은 경우 로그인 폼과 소셜 로그인 버튼을 보여줍니다. */
                    <div>
                        <h2 style={styles.boxTitle}>로그인</h2>
                        {/* 이메일 로그인 폼 */}
                        <form onSubmit={handleEmailLogin} style={styles.form}>
                            <input
                                type="email"
                                placeholder="이메일"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                style={styles.input}
                                required
                            />
                            <input
                                type="password"
                                placeholder="비밀번호"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                style={styles.input}
                                required
                            />
                            <button type="submit" style={{ ...styles.button, ...styles.primaryButton }}>
                                로그인
                            </button>
                        </form>
                        {error && <p style={styles.errorMessage}>{error}</p>}
                        <div style={styles.divider}>
                            <div style={styles.dividerLine} />
                            <span style={styles.dividerText}>또는</span>
                            <div style={styles.dividerLine} />
                        </div>
                        {/* 소셜 로그인 버튼 */}
                        <button
                            onClick={() => handleSocialLogin('google')}
                            style={{ ...styles.button, ...styles.googleButton }}
                        >
                            Google 계정으로 로그인
                        </button>
                        <button
                            onClick={() => handleSocialLogin('naver')}
                            style={{ ...styles.button, ...styles.naverButton }}
                        >
                            Naver 계정으로 로그인
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
}

// LoginPage 컴포넌트를 다른 파일에서 사용할 수 있도록 내보냅니다.
export default LoginPage;
