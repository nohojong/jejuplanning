// React와 react-router-dom에서 필요한 기능들을 가져옵니다.
import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
// api 호출을 위한 모듈을 가져옵니다.
import api from '../api';
// 헤더와 리뷰 카드 컴포넌트를 가져옵니다.
import Header from '../components/Header';
import ReviewCard from '../components/ReviewCard';
import { FiPhone } from 'react-icons/fi';
import MapComponent from '../components/MapComponent';

// 컴포넌트에 사용될 스타일을 정의합니다.
const styles = {
    loading: { textAlign: 'center', padding: '2rem', fontSize: '1.2rem' },
    error: { textAlign: 'center', padding: '2rem', fontSize: '1.2rem', color: 'red' },
    container: { maxWidth: '800px', margin: '0 auto', padding: '2rem' },
    backLink: { display: 'inline-block', marginBottom: '1.5rem', textDecoration: 'none', color: '#007bff', fontWeight: 'bold' },
    placeHeader: { marginBottom: '2rem' },
    placeImage: { width: '100%', height: 'auto', maxHeight: '400px', objectFit: 'cover', borderRadius: '12px', marginBottom: '1rem' },
    title: { fontSize: '2.5rem', margin: '0 0 0.5rem' },
    address: { fontSize: '1.1rem', color: '#555', margin: '0 0 1rem' },
    introduction: { fontSize: '1rem', lineHeight: '1.6', marginBottom: '2rem' },
    reviewsSection: { marginTop: '3rem' },
    reviewsTitle: { fontSize: '1.8rem', borderBottom: '2px solid #eee', paddingBottom: '0.5rem', marginBottom: '1.5rem' },
    infoSection: { display: 'flex', flexDirection: 'column', gap: '0.5rem', marginBottom: '1.5rem', borderTop: '1px solid #eee', paddingTop: '1.5rem' },
    infoItem: { display: 'flex', alignItems: 'center', gap: '0.5rem' },
    phoneLink: { color: '#007bff', textDecoration: 'none' },
};

// 리뷰 작성 폼 컴포넌트입니다.
const ReviewForm = ({ placeId, onReviewSubmit }) => {
    // 컴포넌트의 상태를 관리하는 변수들입니다.
    const [rating, setRating] = useState(5); // 평점
    const [content, setContent] = useState(''); // 리뷰 내용
    const [isSubmitting, setIsSubmitting] = useState(false); // 전송 중 상태
    const [error, setError] = useState(''); // 에러 메시지

    // 폼 제출 시 실행되는 함수입니다.
    const handleSubmit = async (e) => {
        e.preventDefault(); // 폼의 기본 동작(페이지 새로고침)을 막습니다.
        if (content.trim() === '') {
            setError('리뷰 내용을 입력해 주세요.');
            return;
        }

        setIsSubmitting(true);
        setError('');

        try {
            // 서버에 리뷰 데이터를 전송합니다.
            const response = await api.post(`/api/places/${placeId}/reviews`, { rating, content });
            // 리뷰 제출 성공 시 부모 컴포넌트의 콜백 함수를 호출합니다.
            onReviewSubmit(response.data);
            // 입력 필드를 초기화합니다.
            setContent('');
            setRating(5);
        } catch (err) {
            setError('리뷰 작성에 실패했습니다. 잠시 후 다시 시도해주세요');
            console.error('Review submission failed', err);
        } finally {
            setIsSubmitting(false);
        }
    };

    // 리뷰 폼에 사용될 스타일입니다.
    const formStyles = {
        form: { display: 'flex', flexDirection: 'column', gap: '1rem', padding: '1.5rem', backgroundColor: '#f9f9f9', borderRadius: '8px', marginBottom: '2rem' },
        ratingContainer: { display: 'flex', alignItems: 'center', gap: '0.5rem' },
        textarea: { padding: '1rem', borderRadius: '8px', border: '1px solid #ddd', minHeight: '100px', resize: 'vertical' },
        button: { padding: '0.75rem', borderRadius: '8px', border: 'none', backgroundColor: '#FF7F50', color: 'white', fontWeight: 'bold', cursor: 'pointer' },
        error: { color: 'red', fontSize: '0.9rem' }
    };

    // 화면에 보여질 JSX를 반환합니다.
    return (
        <form onSubmit={handleSubmit} style={formStyles.form}>
            <div style={formStyles.ratingContainer}>
                <label htmlFor='rating'>평점:</label>
                <select id="rating" value={rating} onChange={(e) => setRating(Number(e.target.value))}>
                    <option value={5}>★★★★★</option>
                    <option value={4}>★★★★☆</option>
                    <option value={3}>★★★☆☆</option>
                    <option value={2}>★★☆☆☆</option>
                    <option value={1}>★☆☆☆☆</option>
                </select>
            </div>
            <textarea
                placeholder='리뷰를 남겨주세요.'
                value={content}
                onChange={(e) => setContent(e.target.value)}
                style={formStyles.textarea} />
            <button type='submit' disabled={isSubmitting} style={formStyles.button}>
                {isSubmitting ? '등록 중...' : '리뷰 등록'}
            </button>
            {error && <p style={formStyles.error}>{error}</p>}
        </form>
    );
};

// 장소 상세 페이지 컴포넌트입니다.
function PlaceDetailPage() {
    // URL 파라미터에서 장소 ID를 가져옵니다.
    const { id } = useParams();
    // 컴포넌트의 상태를 관리하는 변수들입니다.
    const [place, setPlace] = useState(null); // 장소 정보
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 에러 상태
    const [user, setUser] = useState(null); // 현재 로그인한 사용자 정보

    // 컴포넌트가 렌더링될 때 로그인 상태를 확인하고 데이터를 불러옵니다.
    useEffect(() => {
        const checkLoginAndFetchData = async () => {
            try {
                // 사용자 정보를 가져옵니다.
                const userResponse = await api.get('/api/users/me');
                setUser(userResponse.data);

                // 장소 상세 정보를 가져옵니다.
                const placeResponse = await api.get(`/api/places/${id}`);
                setPlace(placeResponse.data);
            } catch (err) {
                setError('데이터를 불러오는 데 실패했습니다.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        checkLoginAndFetchData();
    }, [id]); // id가 변경될 때마다 이 effect를 다시 실행합니다.

    // 새 리뷰가 제출되었을 때 호출되는 함수입니다.
    const handleReviewSubmitted = (newReview) => {
        setPlace(prevPlace => ({
            ...prevPlace,
            reviews: [newReview, ...prevPlace.reviews] // 기존 리뷰 목록의 맨 앞에 새 리뷰를 추가합니다.
        }));
    };

    // 리뷰가 수정되었을 때 호출되는 함수입니다.
    const handleReviewUpdated = (updatedReview) => {
        setPlace(prevPlace => ({
            ...prevPlace,
            reviews: prevPlace.reviews.map(review =>
                review.id === updatedReview.id ? updatedReview : review // 수정된 리뷰를 찾아 업데이트합니다.
            ),
        }));
    };

    // 리뷰가 삭제되었을 때 호출되는 함수입니다.
    const handleReviewDeleted = (deletedReviewId) => {
        setPlace(prevPlace => ({
            ...prevPlace,
            reviews: prevPlace.reviews.filter(review =>
                review.id !== deletedReviewId // 삭제된 리뷰를 목록에서 제거합니다.
            ),
        }));
    };

    // 로딩 중일 때 보여줄 화면입니다.
    if (loading) return <div style={styles.loading}>로딩 중...</div>;
    // 에러 발생 시 보여줄 화면입니다.
    if (error) return <div style={styles.error}>{error}</div>;
    // 장소 정보가 없을 때 보여줄 화면입니다.
    if (!place) return <div style={styles.error}>장소 정보를 찾을 수 없습니다.</div>;

    // 화면에 보여질 JSX를 반환합니다.
    return (
        <div>
            <Header user={user} />
            <main style={styles.container}>
                <Link to='/places' style={styles.backLink}>← 목록으로 돌아가기</Link>

                <header style={styles.placeHeader}>
                    {place.imgPath && <img src={place.imgPath} alt={place.title} style={styles.placeImage} />}
                    <h1 style={styles.title}>{place.title}</h1>
                    <p style={styles.address}>{place.address}</p>
                </header>

                <section style={styles.infoSection}>
                    {/* <div style={styles.infoItem}>
                        <span>주소: </span>
                        <p style={styles.address}>{place.address}</p>
                    </div> */}
                    {place.phoneNumber && (
                        <div style={styles.infoItem}>
                            <FiPhone />
                            <a href={`tel:${place.phoneNumber}`} style={styles.phoneLink}>
                                {place.phoneNumber}
                            </a>
                        </div>
                    )}
                </section>

                <section>
                    <p style={styles.introduction}>{place.introduction}</p>
                </section>

                {place.latitude && place.longitude && (
                    <section style={{ marginTop: '2rem' }}>
                        <MapComponent
                            latitude={place.latitude}
                            longitude={place.longitude}
                            title={place.title}
                        />
                    </section>
                )}

                <section style={styles.reviewsSection}>
                    <h2 style={styles.reviewsTitle}>리뷰 ({place.reviews.length})</h2>
                    {/* 리뷰 작성 폼 */}
                    <ReviewForm placeId={id} onReviewSubmit={handleReviewSubmitted} />
                    {/* 리뷰 목록 */}
                    {place.reviews.length > 0 ? (
                        <div>
                            {place.reviews.map(review => (
                                <ReviewCard
                                    key={review.id}
                                    review={review}
                                    currentUserEmail={user}
                                    placeId={id}
                                    onUpdate={handleReviewUpdated}
                                    onDelete={handleReviewDeleted}
                                />
                            ))}
                        </div>
                    ) : (
                        <p>작성된 리뷰가 없습니다.</p>
                    )}
                </section>
            </main>
        </div>
    );
}

// PlaceDetailPage 컴포넌트를 다른 파일에서 사용할 수 있도록 내보냅니다.
export default PlaceDetailPage;
