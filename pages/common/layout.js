// common/layout.js
import Head from 'next/head'
import Header from './header'

export default function Layout({ children, pageTitle = 'DiFF' }) {
    return (
        <>
            <Head>
                <meta charSet="UTF-8" />
                <title>{pageTitle}</title>
            </Head>
            {/* 이 안에서만 렌더링 */}
            <div className="text-neutral-600">
                <Header />
                <main>{children}</main>
            </div>
        </>
    )
}
