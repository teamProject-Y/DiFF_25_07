import Layout from '../pages/common/layout';
export default function App({Component, pageProps}) {
    return <Layout><Component {...pageProps} /></Layout>;
}
