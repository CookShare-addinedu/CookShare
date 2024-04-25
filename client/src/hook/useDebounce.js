import { useRef, useState } from 'react';

export const useDebounce = (value) => {
    const [DebounceVal, setDebounceVal] = useState(value);
    const eventBloker = useRef(null);

    clearTimeout(eventBloker.current);

    eventBloker.current = setTimeout(() => {
        setDebounceVal(value);
    }, 500);

    return DebounceVal;
};
