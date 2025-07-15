// middleware.js (프로젝트 루트)
import { NextResponse } from 'next/server'

export function middleware(request) {
    console.log(`[Next.js Middleware] path=${request.nextUrl.pathname}`)
    return NextResponse.next()
}
