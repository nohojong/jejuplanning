// react-router-dom에서 라우팅 관련 컴포넌트들을 가져옵니다.
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
// 각 페이지 컴포넌트들을 가져옵니다.
import LoginPage from './pages/LoginPage';
import PlaceListPage from './pages/PlaceListPage';
import OAuthRedirectionPage from './pages/OAuthRedirectionPage.jsx';
import PlaceDetailPage from './pages/PlaceDetailPage.jsx';

function App() {
  return (
    // BrowserRouter를 사용하여 브라우저의 주소 변경을 감지하고, 그에 맞는 페이지를 보여줍니다.
    <Router>
      {/* Routes는 여러 Route들을 감싸는 컨테이너입니다. */}
      <Routes>
        {/* 각 Route는 특정 경로(path)에 어떤 컴포넌트(element)를 보여줄지 정의합니다. */}
        {/* 루트 경로('/')는 로그인 페이지를 보여줍니다. */}
        <Route path='/' element={<LoginPage />} />
        {/* '/places' 경로는 장소 목록 페이지를 보여줍니다. */}
        <Route path='/places' element={<PlaceListPage />} />
        {/* '/oauth/redirect' 경로는 소셜 로그인 후 리디렉션되는 페이지를 보여줍니다. */}
        <Route path='/oauth/redirect' element={<OAuthRedirectionPage />} />
        {/* '/places/:id' 경로는 장소 상세 페이지를 보여줍니다. :id는 동적인 값(장소의 ID)을 의미합니다. */}
        <Route path='/places/:id' element={<PlaceDetailPage />} />
      </Routes>
    </Router>    
  );
}

// App 컴포넌트를 다른 파일에서 사용할 수 있도록 내보냅니다.
export default App
