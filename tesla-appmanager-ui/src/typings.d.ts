declare module 'slash2';
declare module '*.css';
declare module '*.less';
declare module '*.scss';
declare module '*.sass';
declare module '*.svg';
declare module '*.png';
declare module '*.jpg';
declare module '*.jpeg';
declare module '*.gif';
declare module '*.bmp';
declare module '*.tiff';
declare module 'omit.js';
declare module 'numeral';
declare module 'mockjs';
declare module 'react-fittext';

declare const REACT_APP_ENV: 'test' | 'dev' | 'pre' | false;

export type ResponseBody<T> = {
    code?: number;
    message?: string;
    requestId?: string;
    timestamp?: number;
    data?: T;
};

export type PageData<T> = {
    pageSize: number;
    page: number;
    total: number;
    empty: boolean;
    items: T[];
};

export type PageParam = {
    pageSize?: number;
    page?: number;
    pagination?: boolean;
    withBlobs?: boolean;
};