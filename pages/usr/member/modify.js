import { useState } from 'react'
import { useRouter } from 'next/router'

export default function ModifyPage({ member }) {
    const router = useRouter()
    const [verified, setVerified] = useState(false)
    const [pw, setPw] = useState('')
    const [form, setForm] = useState({
        loginPw: member?.loginPw || '',
        name: member?.name || '',
        nickName: member?.nickName || '',
        cellPhone: member?.cellPhone || '',
        email: member?.email || '',
    })
    const [error, setError] = useState('')

    // 비밀번호 확인
    const handleVerify = async () => {
        setError('')
        try {
            const res = await fetch(
                `http://localhost:3000/member/checkPw?pw=${encodeURIComponent(pw)}`,
                { credentials: 'include' }
            )
            const data = await res.json()
            if (data.resultCode === 'S-1') {
                setVerified(true)
            } else {
                setError('비밀번호가 일치하지 않습니다.')
            }
        } catch {
            setError('검증 중 오류가 발생했습니다.')
        }
    }

    // 수정 폼 필드 변경
    const handleChange = e => {
        setForm({ ...form, [e.target.name]: e.target.value })
    }

    // 정보 수정 제출
    const handleSubmit = async e => {
        e.preventDefault()
        setError('')
        try {
            const res = await fetch('http://localhost:3000/member/doModify', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify(form),
            })
            if (res.ok) {
                router.push('/member/myInfo')
            } else {
                const text = await res.text()
                setError(text || '정보 수정에 실패했습니다')
            }
        } catch {
            setError('서버 요청 중 오류가 발생했습니다.')
        }
    }

    return (
        <div className="container mx-auto mt-10">
            <button
                onClick={() => router.back()}
                className="text-4xl pl-4 mb-4 cursor-pointer"
            >
                <i className="fa-solid fa-angle-left"></i>
            </button>

            {!verified ? (
                <div className="mx-auto max-w-min p-4 bg-neutral-200 border border-neutral-300 rounded-lg">
                    <h2 className="text-2xl font-semibold text-center mb-6">
                        Check Your Password
                    </h2>
                    {error && <p className="text-red-500 mb-4">{error}</p>}
                    <div className="flex flex-col items-center">
                        <input
                            type="password"
                            value={pw}
                            onChange={e => setPw(e.target.value)}
                            className="mb-6 w-96 p-2.5 border border-neutral-300 rounded-lg"
                            placeholder="Password"
                        />
                        <button
                            onClick={handleVerify}
                            className="py-2.5 px-5 w-96 bg-neutral-800 text-neutral-200 rounded-lg hover:bg-neutral-700"
                        >
                            VERIFY
                        </button>
                    </div>
                </div>
            ) : (
                <div className="mx-auto max-w-min p-8 bg-neutral-200 border border-neutral-300 rounded-lg">
                    <h2 className="text-2xl font-semibold text-center mb-6">
                        회원 정보 수정
                    </h2>
                    {error && <p className="text-red-500 mb-4">{error}</p>}
                    <form onSubmit={handleSubmit} className="flex flex-col items-center">
                        <input
                            name="loginId"
                            value={member.loginId}
                            disabled
                            className="mb-4 w-96 p-2.5 border border-neutral-300 rounded-lg bg-neutral-100"
                            placeholder="ID"
                        />
                        <input
                            type="password"
                            name="loginPw"
                            value={form.loginPw}
                            onChange={handleChange}
                            className="mb-4 w-96 p-2.5 border border-neutral-300 rounded-lg"
                            placeholder="Password"
                        />
                        <input
                            name="name"
                            value={form.name}
                            onChange={handleChange}
                            className="mb-4 w-96 p-2.5 border border-neutral-300 rounded-lg"
                            placeholder="Name"
                        />
                        <input
                            name="nickName"
                            value={form.nickName}
                            onChange={handleChange}
                            className="mb-4 w-96 p-2.5 border border-neutral-300 rounded-lg"
                            placeholder="NickName"
                        />
                        <input
                            name="cellPhone"
                            value={form.cellPhone}
                            onChange={handleChange}
                            className="mb-4 w-96 p-2.5 border border-neutral-300 rounded-lg"
                            placeholder="Cell Phone"
                        />
                        <input
                            type="email"
                            name="email"
                            value={form.email}
                            onChange={handleChange}
                            className="mb-6 w-96 p-2.5 border border-neutral-300 rounded-lg"
                            placeholder="E-mail"
                        />
                        <button
                            type="submit"
                            className="py-2.5 px-5 w-96 bg-neutral-800 text-neutral-200 rounded-lg hover:bg-neutral-700"
                        >
                            UPDATE
                        </button>
                    </form>
                </div>
            )}
        </div>
    )
}

// SSR로 로그인된 사용자 정보 가져오기
export async function getServerSideProps(context) {
    const res = await fetch('http://localhost:3000/member/myInfo', {
        headers: { cookie: context.req.headers.cookie || '' },
        credentials: 'include',
    })
    if (res.status !== 200) {
        return { redirect: { destination: '/member/login', permanent: false } }
    }
    const member = await res.json()
    return { props: { member } }
}

ModifyPage.pageTitle = 'MY INFO MODIFY'
