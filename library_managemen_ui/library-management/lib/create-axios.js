import axios from 'axios';

export const myAxios = axios.create({
    baseURL: `${process.env.NEXT_PUBLIC_BASE_URL}`
});