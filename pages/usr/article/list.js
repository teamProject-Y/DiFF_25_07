import { useState } from 'react'
import { useRouter } from 'next/router'
import Layout from '../common/layout'
import Header from '../common/header'

export default function ArticleListPage({ articles, totalCnt, totalPage, page, boardId, searchItem, keyword }) {
    const router = useRouter()

    const handleSearch = e => {
        e.preventDefault()
        const form = e.target
        const params = new URLSearchParams({
            boardId: form.boardId.value,
            searchItem: form.searchItem.value,
            keyword: form.keyword.value,
            page: '1',
        })
        router.push(`/usr/article/list?${params.toString()}`)
    }

    return (
        <Layout pageTitle="ARTICLE LIST">
            <Header />
            <button onClick={() => router.back()} className="block text-4xl pl-10 cursor-pointer">
                <i className="fa-solid fa-angle-left"></i>
            </button>

            <div className="container mx-auto my-10 w-5/6">
                <span className="text-4xl font-bold m-4">Article list</span>
                <div className="flex justify-between items-end text-neutral-800 mx-2 my-3">
                    <div className="articleCnt font-normal mx-4">총 게시 글 : {totalCnt}</div>
                    <form onSubmit={handleSearch} className="search-bar flex justify-between items-center h-8 px-2 text-sm">
                        <select name="boardId" defaultValue={boardId} className="px-4 border rounded-lg">
                            <option value="0">전체 게시판</option>
                            <option value="1">공지사항</option>
                            <option value="2">자유 게시판</option>
                            <option value="3">QnA</option>
                        </select>
                        <select name="searchItem" defaultValue={searchItem} className="mx-2 border rounded-md pl-3 pr-1">
                            <option value="1">제목</option>
                            <option value="2">내용</option>
                            <option value="3">작성자</option>
                        </select>
                        <input name="keyword" defaultValue={keyword} placeholder="Search" className="border mx-2 rounded-md pl-3 pr-1" />
                        <button type="submit" className="p-2 bg-neutral-800 text-neutral-200 rounded-md hover:bg-neutral-700">
                            🔍
                        </button>
                        <a href="/usr/article/write" className="ml-6 px-5 text-base rounded-md hover:bg-neutral-300">글 작성</a>
                    </form>
                </div>

                <div className="overflow-hidden rounded-xl border">
                    <table className="w-full text-sm text-center text-neutral-800">
                        <thead className="bg-neutral-800 text-neutral-200">
                        <tr>
                            {['NO','BOARD','TITLE','WRITER','sumReaction','goodReaction','badReaction','HITS','REGISTRATION DATE'].map((h, i)=>(
                                <th key={i} className="px-6 py-4">{h}</th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {articles.length>0 ? articles.map(article=> (
                            <tr key={article.id} className="border-b bg-neutral-200 hover:bg-neutral-300 cursor-pointer" onClick={()=>router.push(`/article/detail?id=${article.id}`)}>
                                <td className="px-5 py-3">{article.id}</td>
                                <td className="px-5 py-3">{article.extra_boardCode}</td>
                                <td className="px-5 py-3 text-left pl-6">{article.title}</td>
                                <td className="px-5 py-3">{article.extra_writer}</td>
                                <td className="px-5 py-3">{article.extra_sumReactionPoint}</td>
                                <td className="px-5 py-3">{article.extra_goodReactionPoint}</td>
                                <td className="px-5 py-3">{article.extra_badReactionPoint}</td>
                                <td className="px-5 py-3">{article.hits}</td>
                                <td className="px-5 py-3">{article.regDate.substring(0,10)}</td>
                            </tr>
                        )) : (
                            <tr><td colSpan={9} className="py-8 text-lg">게시글이 없습니다</td></tr>
                        )}
                        </tbody>
                    </table>
                </div>

                <div className="inline-flex justify-center text-xl mt-4">
                    {[...Array(totalPage)].map((_, idx)=>(
                        <button key={idx} onClick={()=>router.push(`/usr/article/list?boardId=${boardId}&searchItem=${searchItem}&keyword=${keyword}&page=${idx+1}`)}
                                className={`mx-1 w-8 ${page===idx+1?'bg-neutral-200 rounded-full':''}`}>{idx+1}</button>
                    ))}
                </div>
            </div>
        </Layout>
    )
}

export async function getServerSideProps({ query }) {
    const { boardId='0', searchItem='1', keyword='', page='1' } = query
    const res = await fetch(`http://localhost:8080/article/list?boardId=${boardId}&searchItem=${searchItem}&keyword=${encodeURIComponent(keyword)}&page=${page}`)
    const data = await res.json()
    return { props: { ...data, boardId, searchItem, keyword, page: parseInt(page) } }
}
