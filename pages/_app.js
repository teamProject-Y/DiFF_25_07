// pages/_app.js
import AppBase from 'next/app'
import Script from 'next/script'
import Layout from '../pages/common/layout'

function MyApp({ Component, pageProps, member }) {
    return (
        <>
            <Script
                src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
                strategy="beforeInteractive"
            />
            <Layout member={member} pageTitle={Component.pageTitle}>
                <Component {...pageProps} />
            </Layout>
        </>
    )
}

// getInitialProps를 사용해서 SSR 단계에서 로그인된 회원 정보(myInfo)를 가져옵니다
MyApp.getInitialProps = async (appContext) => {
    const appProps = await AppBase.getInitialProps(appContext)
    const { ctx } = appContext
    const cookie = ctx.req?.headers.cookie || ''
    let member = null

    try {
        const res = await fetch('http://localhost:8080/usr/member/myInfo', {
            headers: { cookie },
            credentials: 'include',
        })
        if (res.ok) {
            member = await res.json()
        }
    } catch (e) {
        // 로그인 안 된 경우나 에러면 member = null
    }

    return {
        ...appProps,
        member,
    }
}

export default MyApp
