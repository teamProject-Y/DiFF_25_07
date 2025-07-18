// pages/usr/article/detail.js
import { useState, useEffect } from 'react'
import { useRouter } from 'next/router'
import { getSession } from 'next-auth/react'
import Layout from '../../../common/layout'

export default function ArticleDetailPage({ member, article, comment }) {
    const router = useRouter()
    const { id } = router.query

    const [article, setArticle] = useState(article)
    const [comment, setComment] = useState(comment)

    // BASE_URL 환경변수 또는 로컬
    const API_BASE = process.env.BACKEND_URL || 'http://localhost:8080'

    // 1) 조회수 1회만 증가
    useEffect(() => {
        if (!id) return
        const key = `article__${id}__viewed`
        if (localStorage.getItem(key)) return
        localStorage.setItem(key, '1')

        fetch(`${API_BASE}/usr/article/${id}/hit`, {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
        })
            .then(r => r.json())
            .then(rd => {
                if (rd.resultCode.startsWith('S-')) {
                    setArticle(a => ({ ...a, hits: rd.data1 }))
                }
            })
    }, [id])

    // 2) 좋아요 토글
    const toggleLike = () => {
        fetch(`${API_BASE}/usr/article/${id}/like`, {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
        })
            .then(r => r.json())
            .then(rd => {
                if (rd.resultCode.startsWith('S-')) {
                    setArticle(a => ({
                        ...a,
                        extra_goodReactionPoint: rd.data.likeCount,
                        // userReaction 은 서버가 관리하므로, 단순히 반전시켜 보기
                        userReaction: a.userReaction === 1 ? 0 : 1,
                    }))
                }
            })
    }

    // 3) 댓글 작성
    const handleCommentSubmit = e => {
        e.preventDefault()
        const body = e.target.body.value.trim()
        if (!body) return

        fetch(`${API_BASE}/usr/article/${id}/comment`, {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ body }),
        })
            .then(r => r.json())
            .then(rd => {
                if (rd.resultCode.startsWith('S-')) {
                    // 댓글 다시 불러오기
                    fetch(`${API_BASE}/usr/article/${id}`, {
                        credentials: 'include',
                    })
                        .then(r => r.json())
                        .then(data => setComment(data.comment))
                    e.target.body.value = ''
                }
            })
    }

    return (
        <Layout pageTitle="ARTICLE DETAIL" member={member}>
            <button onClick={() => router.back()} className="text-4xl pl-10 cursor-pointer">
                ←
            </button>

            <div className="container mx-auto my-6 p-6 bg-neutral-100 rounded-xl">
                <h1 className="text-3xl font-bold mb-2">{article.title}</h1>
                <div className="text-sm text-neutral-600 mb-4">
                    작성일: {article.regDate.substring(0, 10)} | 조회수: {article.hit}
                </div>

                <div className="prose mb-4">{article.body}</div>

                <div className="flex items-center space-x-4 mb-6">
                    <button
                        onClick={toggleLike}
                        className={`px-4 py-2 border rounded ${article.userReaction === 1 ? 'bg-neutral-300' : ''}`}
                    >
                        👍 {article.extra_goodReactionPoint}
                    </button>
                </div>

                <hr />

                <section className="mt-6">
                    <h2 className="text-2xl mb-4">Comment</h2>

                    {comment.map(c => (
                        <div key={c.id} className="mb-4 p-4 border rounded">
                            <strong>{c.extra_writer}</strong>
                            <p>{c.body}</p>
                        </div>
                    ))}

                    <form onSubmit={handleCommentSubmit} className="mt-6">
                        <input
                            name="body"
                            type="text"
                            placeholder="나도 한마디 하기!"
                            className="w-full p-2 border rounded mb-2"
                        />
                        <button type="submit" className="px-4 py-2 bg-neutral-800 text-white rounded">
                            게시
                        </button>
                    </form>
                </section>
            </div>
        </Layout>
    )
}

ArticleDetailPage.pageTitle = 'ARTICLE DETAIL'

export async function getServerSideProps({ query, req }) {
    const { id } = query
    const API_BASE = process.env.BACKEND_URL || 'http://localhost:8080'

    // 로그인 체크
    const meRes = await fetch(`${API_BASE}/usr/member/myInfo`, {
        headers: { cookie: req.headers.cookie || '' },
        credentials: 'include',
        redirect: 'manual',
    })
    if (meRes.status !== 200) {
        return { redirect: { destination: '/usr/member/login', permanent: false } }
    }
    const member = await meRes.json()

    // 글 + 댓글
    const detailRes = await fetch(`${API_BASE}/usr/article/${id}`, {
        headers: { cookie: req.headers.cookie || '' },
        credentials: 'include',
    })
    if (!detailRes.ok) {
        return { notFound: true }
    }
    const data = await detailRes.json()

    return {
        props: {
            member,
            initialArticle: data.article,
            initialComments: data.comment,
        },
    }
}
