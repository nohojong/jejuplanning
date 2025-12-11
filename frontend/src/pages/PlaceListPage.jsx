// React와 react-router-dom에서 필요한 기능들을 가져옵니다.
import { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
// api 호출을 위한 모듈을 가져옵니다.
import api from '../api';
// 헤더 컴포넌트를 가져옵니다.
import Header from '../components/Header';

// 컴포넌트에 사용될 스타일을 정의합니다.
const styles = {
    container: { maxWidth: '800px', margin: '0 auto', padding: '2rem' },
    searchBar: { display: 'flex', marginBottom: '2rem', gap: '0.5rem' },
    searchInput: { flex: 1, padding: '0.75rem', fontSize: '1rem', border: '1px solid #ccc', borderRadius: '8px' },
    searchButton: { padding: '0.75rem 1.5rem', border: 'none', backgroundColor: '#007bff', color: 'white', borderRadius: '8px', cursor: 'pointer' },
    placeList: { display: 'grid', gridTemplateColumns: '1fr', gap: '1.5rem' },
    placeCard: {
        display: 'flex',
        backgroundColor: 'white',
        borderRadius: '12px',
        boxShadow: '0 4px 8px rgba(0,0,0,0.1)',
        overflow: 'hidden',
        textDecoration: 'none',
        color: 'inherit',
        transition: 'transform 0.2s',
    },
    thumbnail: { width: '120px', height: '120px', objectFit: 'cover' },
    cardContent: { padding: '1rem' },
    cardTitle: { margin: '0 0 0.5rem', fontSize: '1.2rem' },
    cardAddress: { margin: '0', fontSize: '0.9rem', color: '#666' },
    loading: { textAlign: 'center', padding: '2rem', fontSize: '1.2rem' },
    error: { textAlign: 'center', padding: '2rem', fontSize: '1.2rem', color: 'red' },
    loadMoreButton: { 
        display: 'block',
        width: '100%',
        padding: '1rem',
        marginTop: '1rem',
        backgroundColor: '#f0f0f0',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '1rem'
      }
};

// 장소 목록을 보여주는 페이지 컴포넌트입니다.
function PlaceListPage() {
    // 컴포넌트의 상태를 관리하는 변수들입니다.
    const [places, setPlaces] = useState([]); // 장소 목록
    const [page, setPage] = useState(0); // 현재 페이지 번호
    const [hasMore, setHasMore] = useState(true); // 더 불러올 데이터가 있는지 여부
    const [searchTerm, setSearchTerm] = useState(''); // 검색어
    const [loading, setLoading] = useState(false); // 로딩 상태
    const [error, setError] = useState(null); // 에러 상태
    const [user, setUser] = useState(null); // 현재 로그인한 사용자 정보

    // 컴포넌트가 처음 렌더링될 때 로그인 상태를 확인합니다.
    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                // 서버에 사용자 정보를 요청합니다.
                const response = await api.get('/api/users/me');
                setUser(response.data);
            } catch (err) {
                // 사용자 정보 요청에 실패하면 로그인 페이지로 이동합니다.
                window.location.href = '/';
            }
        };
        checkLoginStatus();
    }, []);

    // 컴포넌트가 처음 렌더링될 때 장소 목록을 불러옵니다.
    useEffect(() => {
        fetchPlaces(searchTerm, 0);
    }, []);


    // 서버에서 장소 목록 데이터를 불러오는 함수입니다.
    const fetchPlaces = useCallback(async(searchTerm, pageToFetch) => {
        // 로딩 상태를 true로 설정하고 에러를 초기화합니다.
        setLoading(true);
        setError(null);

        try {
            // 검색어가 있으면 검색 API를, 없으면 일반 목록 API를 호출합니다.
            const url = searchTerm
                ? `/api/places/search?title=${searchTerm}&page=${pageToFetch}&size=10`
                : `/api/places?page=${pageToFetch}&size=10`;

            const response = await api.get(url);
            const data = response.data;

            // 첫 페이지를 불러오는 경우, 장소 목록을 새로 설정합니다.
            if (pageToFetch === 0) {
                setPlaces(data.content);
            } else {
                // 다음 페이지를 불러오는 경우, 기존 목록에 추가합니다.
                setPlaces(prevPlaces => [...prevPlaces, ...data.content]);
            }
            // 마지막 페이지인지 여부를 설정합니다.
            setHasMore(!data.last);
            // 현재 페이지 번호를 업데이트합니다.
            setPage(pageToFetch);
        } catch (err) {
            setError('장소 목록을 불러오는 데 실패했습니다.');
            console.error(err);
        } finally {
            // 로딩 상태를 false로 설정합니다.
            setLoading(false);
        }
    }, []);

    // 사용자 정보가 변경되면 장소 목록을 다시 불러옵니다.
    useEffect(() => {
        if (user) {
            fetchPlaces(searchTerm, 0);
        }
    }, [user]);

    // 검색 버튼을 눌렀을 때 실행되는 함수입니다.
    const handleSearch = (e) => {
        e.preventDefault(); // 폼의 기본 동작(페이지 새로고침)을 막습니다.
        setPage(0); // 페이지 번호를 0으로 초기화합니다.
        fetchPlaces(searchTerm, 0); // 검색어로 장소 목록을 불러옵니다.
    };

    // '더보기' 버튼을 눌렀을 때 다음 페이지의 데이터를 불러오는 함수입니다.
    const loadMore = () => {
        fetchPlaces(searchTerm, page + 1);
    }

    // 사용자 정보가 없으면 인증 확인 중 메시지를 보여줍니다.
    if (!user) {
        return <div style={styles.loading}>인증 확인 중...</div>;
    }

    // 화면에 보여질 JSX를 반환합니다.
    return (
        <div>
            {/* 헤더 컴포넌트 */}
            <Header user={user} />
            <main style={styles.container}>
                {/* 검색 폼 */}
                <form onSubmit={handleSearch} style={styles.searchBar}>
                    <input
                        type='text'
                        placeholder="장소 이름으로 검색"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        style={styles.searchInput}
                    />
                    <button type="submit" style={styles.searchButton}>검색</button>
                </form>

                {/* 로딩 및 에러 메시지 */}
                {places.length === 0 && loading && <div style={styles.loading}>로딩 중...</div>}
                {error && <div style={styles.error}>{error}</div>}

                {/* 장소 목록 */}
                <div style={styles.placeList}>
                    {places.map(place => (
                        // 각 장소를 클릭하면 상세 페이지로 이동하는 링크
                        <Link to={`/places/${place.id}`} key={place.id} style={styles.placeCard}>
                            <img src={place.thumbnailPath} alt={place.title} style={styles.thumbnail} />
                            <div style={styles.cardContent}>
                                <h3 style={styles.cardTitle}>{place.title}</h3>
                                <p style={styles.cardAddress}>{place.address}</p>
                            </div>
                        </Link>
                    ))}
                </div>

                {/* '더보기' 버튼 */}
                {hasMore && (
                    <button onClick={loadMore} disabled={loading} style={styles.loadMoreButton}>
                        {loading ? '로딩 중...' : '더보기'}
                    </button>
                )}
            </main>
        </div>
    );
}

// PlaceListPage 컴포넌트를 다른 파일에서 사용할 수 있도록 내보냅니다.
export default PlaceListPage;
