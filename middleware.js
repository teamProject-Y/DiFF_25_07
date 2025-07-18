// middleware.js (프로젝트 루트)
import { NextResponse } from 'next/server'
import { withAuth } from "next-auth/middleware"

// 기존에 있었음
function loggerMiddleware(request) {
    console.log(`[Next.js Middleware] path=${request.nextUrl.pathname}`)
    return NextResponse.next()
}

// withAuth로 래핑해서 기본 export
export default withAuth(loggerMiddleware, {
    callbacks: {
        // 토큰이 있으면 허용
        authhorized: ({ token }) => !!token,
    },
})

// 보호할 경로 매처
export const config = {
    matcher: ["/dashboard/:path*", "/settings/:path*"],
}