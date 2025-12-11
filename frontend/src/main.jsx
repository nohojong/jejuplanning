// React의 StrictMode를 가져옵니다. StrictMode는 잠재적인 문제를 알아내기 위한 도구입니다.
import { StrictMode } from 'react'
// react-dom/client에서 createRoot를 가져옵니다. 이는 React 18의 새로운 Concurrent Mode를 활성화하는 데 사용됩니다.
import { createRoot } from 'react-dom/client'
// 전역 CSS 파일을 가져옵니다.
import './index.css'
// 메인 애플리케이션 컴포넌트를 가져옵니다.
import App from './App.jsx'

// HTML의 'root' 요소를 찾아 React 애플리케이션을 렌더링합니다.
createRoot(document.getElementById('root')).render(
  // StrictMode로 App 컴포넌트를 감싸서 개발 중에 잠재적인 문제를 검사합니다.
  <StrictMode>
    <App />
  </StrictMode>,
)