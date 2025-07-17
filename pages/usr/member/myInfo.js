// pages/usr/member/myInfo.js
import Link from 'next/link'

// 이 페이지에서 보여줄 title
function MyInfo({ member }) {
    return (
        <>
            <button
                onClick={() => window.history.back()}
                className="block text-4xl pl-10 cursor-pointer mb-4"
            >
                <i className="fa-solid fa-angle-left"></i>
            </button>

            <div className="flex items-end mb-6">
                <div className="avatar mr-4">
                    <div className="w-24 rounded-lg overflow-hidden">
                        <img
                            src={member.profileUrl || 'https://img.daisyui.com/images/profile/demo/batperson@192.webp'}
                            alt="avatar"
                        />
                    </div>
                </div>
                <div className="text-xl">
                    <strong>{member.nickName}</strong> 님 반갑습니다.
                </div>
                <div className="flex-grow"></div>
                <Link href="/usr/member/modify">
                    <a className="px-4 py-2 border rounded hover:bg-neutral-300">
                        회원 정보 수정
                    </a>
                </Link>
            </div>

            <div>
                <div className="mb-2 text-lg font-bold">회원 정보</div>
                <table className="bg-neutral-300 rounded-xl w-full text-left">
                    <tbody>
                    <tr>
                        <th className="px-6 py-4">이름</th>
                        <td className="px-6 py-4">{member.name}</td>
                    </tr>
                    <tr>
                        <th className="px-6 py-4">전화번호</th>
                        <td className="px-6 py-4">{member.cellPhone}</td>
                    </tr>
                    <tr>
                        <th className="px-6 py-4">이메일</th>
                        <td className="px-6 py-4">{member.email}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </>
    )
}

// getServerSideProps로 Spring Boot에서 데이터 받아오기
export async function getServerSideProps(context) {
    const res = await fetch('http://localhost:8080/usr/member/myInfo', {
        headers: { cookie: context.req.headers.cookie || '' },
    })
    if (res.status !== 200) {
        return {
            redirect: { destination: '/login', permanent: false },
        }
    }
    const { member } = await res.json()
    return { props: { member } }
}

// Layout에서 쓸 페이지 타이틀
MyInfo.pageTitle = 'MY PAGE'

export default MyInfo
