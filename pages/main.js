// pages/usr/home/main.js

export default function HomeMainPage({ member }) {
    return (
        <>
            <h1>🎉 로그인 성공!</h1>
            <p>닉네임: {member.nickName}</p>
            <p>이메일: {member.email}</p>
            <a href="/logout" style={{ color: 'red', fontWeight: 'bold' }}>🔒 로그아웃</a>
        </>
    )
}

// _app.js에서 페이지별 타이틀을 감싸주도록 설정
HomeMainPage.pageTitle = 'Main'

export async function getServerSideProps(context) {
    // 쿠키를 통해 세션 인증
    const res = await fetch('http://localhost:8080/usr/member/myInfo', {
        headers: { cookie: context.req.headers.cookie || '' },
    })

    if (res.status !== 200) {
        return {
            redirect: {
                destination: '/login',
                permanent: false,
            },
        }
    }

    const data = await res.json()
    return {
        props: {
            member: data.member,
        },
    }
}
