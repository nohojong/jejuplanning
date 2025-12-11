// React와 react-router-dom에서 필요한 기능들을 가져옵니다.
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
// api 호출을 위한 모듈을 가져옵니다.
import api from '../api';

// 컴포넌트에 사용될 스타일을 정의합니다.
const styles = {
    container: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        fontSize: '1.5rem',
    },
};

// 소셜 로그인 후 리디렉션되는 페이지 컴포넌트입니다.
function OAuthRedirectionPage() {
    // 페이지 이동을 위한 navigate 함수를 사용합니다.
    const navigate = useNavigate();

    // 컴포넌트가 렌더링될 때 로그인 상태를 확인하고 리디렉션합니다.
    useEffect(() => {
        const checkLoginAndRedirect = async () => {
            try {
                // 서버에 사용자 정보를 요청하여 로그인 상태를 확인합니다.
                await api.get('/api/users/me');
                // 로그인 성공 시 장소 목록 페이지로 이동합니다.
                navigate('/places');
            } catch (error) {
                // 로그인 실패 시 에러를 출력하고 로그인 페이지로 이동합니다.
                console.error("소셜 로그인 후 상태 확인 실패: ", error);
                navigate('/');
            }
        };
        checkLoginAndRedirect();
    }, [navigate]);

    // 화면에 보여질 JSX를 반환합니다.
    return (
        <div style={styles.container}>
          로그인 처리 중입니다...
        </div>
    );
}

// OAuthRedirectionPage 컴포넌트를 다른 파일에서 사용할 수 있도록 내보냅니다.
export default OAuthRedirectionPage;
