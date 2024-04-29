import './Badge.scss';
export default function Badge({food}) {
    const badgeClass = food.status === '나눔하기' ? 'badge_ongoing' : 'badge_done';
    return (
        <p className={`badge ${badgeClass}`}>{food.status}</p>
    );
}
