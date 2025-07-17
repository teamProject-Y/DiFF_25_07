import Link from 'next/link'
import { useRouter } from 'next/router'
import Layout from '../common/layout'

export default function MyInfoPage({ member }) {
    const router = useRouter()

    return (
        <Layout pageTitle="MYPAGE" member={member}>
            <section className="mt-24 text-xl px-4">
                <div className="mx-auto max-w-4xl">
                    <table className="w-full border-collapse border border-neutral-300 mb-6">
                        <tbody>
                        <tr>
                            <th className="border p-2">가입일</th>
                            <td className="border p-2 text-center">{member.regDate}</td>
                        </tr>
                        <tr>
                            <th className="border p-2">아이디</th>
                            <td className="border p-2 text-center">{member.loginId}</td>
                        </tr>
                        <tr>
                            <th className="border p-2">이름</th>
                            <td className="border p-2 text-center">{member.name}</td>
                        </tr>
                        <tr>
                            <th className="border p-2">닉네임</th>
                            <td className="border p-2 text-center">{member.nickName}</td>
                        </tr>
                        <tr>
                            <th className="border p-2">이메일</th>
                            <td className="border p-2 text-center">{member.email}</td>
                        </tr>
                        <tr>
                            <th className="border p-2">전화번호</th>
                            <td className="border p-2 text-center">{member.cellPhone}</td>
                        </tr>
                        <tr>
                            <th className="border p-2">회원정보 수정</th>
                            <td className="border p-2 text-center">
                                <Link href="/usr/member/modify">
                                    <a className="px-4 py-2 bg-blue-600 text-white rounded">수정</a>
                                </Link>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <div className="text-center">
                        <button
                            onClick={() => router.back()}
                            className="px-6 py-2 text-sm bg-neutral-800 text-white rounded hover:bg-neutral-700"
                        >
                            뒤로가기
                        </button>
                    </div>
                </div>
            </section>
        </Layout>
    )
}

export async function getServerSideProps(context) {
    const res = await fetch('http://localhost:8080/usr/member/myInfo', {
        headers: { cookie: context.req.headers.cookie || '' },
        credentials: 'include',
    })
    if (res.status !== 200) {
        return { redirect: { destination: '/usr/member/login', permanent: false } }
    }
    const member = await res.json()
    return { props: { member } }
}
