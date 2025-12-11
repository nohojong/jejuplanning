// React에서 useState 훅을 가져옵니다.
import { useState } from 'react';
// API 호출을 위한 모듈을 가져옵니다.
import api from '../api';

// 별점 표시 컴포넌트입니다.
const StarRating = ({ rating, setRating, isEditing }) => {
    const styles = {
        starRating: { color: '#f8d22f', marginBottom: '0.5rem', cursor: isEditing ? 'pointer' : 'default', fontSize: '1.2rem' },
    };

    // 수정 모드일 경우, 클릭하여 별점을 변경할 수 있습니다.
    if (isEditing) {
        return (
            <div style={styles.starRating}>
                {[5, 4, 3, 2, 1].map(star => (
                    <span key={star} onClick={() => setRating(star)}>
                        {rating >= star ? '★' : '☆'}
                    </span>
                ))}
            </div>
        );
    }

    // 일반 모드일 경우, 별점을 표시만 합니다.
    return <div style={styles.starRating}>
        {'★'.repeat(rating)}{'☆'.repeat(5 - rating)}
    </div>;
};

// 컴포넌트에 사용될 스타일을 정의합니다.
const styles = {
    reviewCard: { backgroundColor: 'white', padding: '1.5rem', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.05)', marginBottom: '1rem' },
    reviewHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' },
    reviewer: { fontWeight: 'bold', color: '#333' },
    reviewDate: { fontSize: '0.8rem', color: '#888' },
    reviewContent: { fontSize: '1rem', color: '#444', marginTop: '0.75rem', whiteSpace: 'pre-wrap' },
    buttonGroup: { display: 'flex', gap: '0.5rem', marginTop: '1rem' },
    button: { padding: '0.3rem 0.7rem', fontSize: '0.8rem', border: '1px solid #ddd', borderRadius: '4px', cursor: 'pointer', backgroundColor: 'white' },
    textarea: { width: '100%', padding: '1rem', borderRadius: '8px', border: '1px solid #ddd', minHeight: '100px', resize: 'vertical' },
};

// 리뷰 하나를 표시하는 카드 컴포넌트입니다.
function ReviewCard({ review, currentUserEmail, placeId, onUpdate, onDelete }) {
    // 컴포넌트의 상태를 관리하는 변수들입니다.
    const [isEditing, setIsEditing] = useState(false); // 수정 모드 여부
    const [editedContent, setEditedContent] = useState(review.content); // 수정된 리뷰 내용
    const [editedRating, setEdiedRating] = useState(review.rating); // 수정된 평점

    // 현재 로그인한 사용자가 리뷰 작성자인지 확인합니다.
    const isOwner = review.reviewer === currentUserEmail;

    // 날짜와 시간을 포맷하는 함수입니다.
    const formatDateTime = (createdAt, updatedAt) => {
        const isUpdated = new Date(updatedAt).getTime() - new Date(createdAt).getTime() > 1000;
        const dateToShow = isUpdated ? updatedAt : createdAt;
        const date = new Date(dateToShow);
        const formattedDate =
            `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`;
        return isUpdated ? `${formattedDate} (수정됨)` : formattedDate;
    };

    // 리뷰 수정 핸들러입니다.
    const handleUpdate = async () => {
        try {
            // 서버에 수정된 리뷰 데이터를 전송합니다.
            const response = await api.put(`/api/places/${placeId}/reviews/${review.id}`, {
                rating: editedRating,
                content: editedContent
            });

            // 부모 컴포넌트의 onUpdate 콜백을 호출하여 UI를 업데이트합니다.
            onUpdate(response.data);
            setIsEditing(false); // 수정 모드를 종료합니다.
        } catch (error) {
            console.error('리뷰 수정 실패:', error);
            alert('리뷰 수정에 실패했습니다.');
        }
    };

    // 리뷰 삭제 핸들러입니다.
    const handleDelete = async () => {
        if (window.confirm('리뷰를 삭제하시겠습니까?')) {
            try {
                // 서버에 리뷰 삭제를 요청합니다.
                await api.delete(`/api/places/${placeId}/reviews/${review.id}`);
                // 부모 컴포넌트의 onDelete 콜백을 호출하여 UI를 업데이트합니다.
                onDelete(review.id);
            } catch (error) {
                console.error('리뷰 삭제 실패:', error);
                alert('리뷰 삭제에 실패했습니다.');
            }
        }
    };

    // 화면에 보여질 JSX를 반환합니다.
    return (
        <div style={styles.reviewCard}>
            <div style={styles.reviewHeader}>
                <span style={styles.reviewer}>{review.reviewer}</span>
                <span style={styles.reviewDate}>
                    {formatDateTime(review.createdAt, review.updatedAt)}
                </span>
            </div>
            {/* 수정 모드일 때와 아닐 때 다른 UI를 보여줍니다. */}
            {isEditing ? (
                <>
                    <StarRating rating={editedRating} setRating={setEdiedRating} isEditing={true} />
                    <textarea
                        value={editedContent}
                        onChange={(e) => setEditedContent(e.target.value)}
                        style={styles.textarea} />
                    <div style={styles.buttonGroup}>
                        <button onClick={handleUpdate} style={styles.button}>저장</button>
                        <button onClick={() => setIsEditing(false)} style={styles.button}>취소</button>
                    </div>
                </>
            ) : (
                <>
                    <StarRating rating={review.rating} />
                    <p style={styles.reviewContent}>{review.content}</p>
                    {/* 리뷰 작성자일 경우에만 수정/삭제 버튼을 보여줍니다. */}
                    {isOwner && (
                        <div style={styles.buttonGroup}>
                            <button onClick={() => setIsEditing(true)} style={styles.button}>수정</button>
                            <button onClick={handleDelete} style={styles.button}>삭제</button>
                        </div>
                    )}
                </>
            )}
        </div>
    );
}

// ReviewCard 컴포넌트를 다른 파일에서 사용할 수 있도록 내보냅니다.
export default ReviewCard;
