import { useEffect, useState } from 'react';
import { Map, MapMarker } from 'react-kakao-maps-sdk';

const { kakao } = window;

function MapComponent({ latitude, longitude, title }) {

    const [isMapReady, setIsMapReady] = useState(false);

    useEffect(() => {

        if (window.kakao && window.kakao.maps) {
            kakao.maps.load(() => {
               setIsMapReady(true);
            });
        }        
    }, []);

    if (!isMapReady) {
        return <div>지도 로딩 중...</div>
    }

    return (
        <Map
        center={{lat: latitude, lng: longitude}}
        style={{ width: '100%', height: '400px',  borderRadius: '12px', }}
        level={3}>
            <MapMarker position={{ lat: latitude, lng: longitude}}>
                <div style={{padding: "5px", color:"#000"}}>{title}</div>
            </MapMarker>
        </Map>
    );
}

export default MapComponent;