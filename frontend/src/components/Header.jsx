// react-router-dom에서 Link 컴포넌트를 가져옵니다. Link는 페이지 이동을 위해 사용됩니다.
import { Link } from 'react-router-dom';
// API 호출을 위한 모듈을 가져옵니다.
import api from '../api';

// 컴포넌트에 사용될 스타일을 정의합니다.
const styles = {
    header: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '1rem 2rem',
        backgroundColor: 'white',
        boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
        position: 'sticky',
        top: 0,
        zIndex: 10,
    },
    logo: {
        fontSize: '1.5rem',
        fontWeight: 'bold',
        color: '#FF7F50',
        textDecoration: 'none',
    },
    nav: {
        display: 'flex',
        alignItems: 'center',
        gap: '1rem',
    },
    welcome: {
        fontSize: '0.9rem',
    },
    logoutButton: {
        padding: '0.5rem 1rem',
        border: 'none',
        borderRadius: '8px',
        backgroundColor: '#6c757d',
        color: 'white',
        cursor: 'pointer',
    }
};

// 헤더 컴포넌트입니다. 사용자 정보와 로그아웃 기능을 props로 받습니다.
function Header({ user, onLogout }) {

    // 로그아웃 버튼 클릭 시 실행되는 함수입니다.
    const handleLogout = async () => {
        try {
            // 서버에 로그아웃을 요청합니다.
            await api.post('/api/auth/logout');
            // onLogout 콜백 함수가 있으면 실행합니다.
            if (onLogout) onLogout();
            // 로그인 페이지로 이동합니다.
            window.location.href = '/';
        } catch (err) {
            console.error('Logout failed:', err);
        }
    };

    // 화면에 보여질 JSX를 반환합니다.
    return (
        <header style={styles.header}>
            {/* 로고를 클릭하면 장소 목록 페이지로 이동합니다. */}
            <Link to='/places' style={styles.logo}>JeJu Trip</Link>
            {/* 사용자 정보가 있을 경우, 환영 메시지와 로그아웃 버튼을 보여줍니다. */}
            {user && (
                <nav style={styles.nav}>
                    <span style={styles.welcome}>{user}</span>
                    <button onClick={handleLogout} style={styles.logoutButton}>로그아웃</button>
                </nav>
            )}
        </header>
    );
}

// Header 컴포넌트를 다른 파일에서 사용할 수 있도록 내보냅니다.
export default Header;
