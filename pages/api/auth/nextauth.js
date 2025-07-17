import NextAuth from "next-auth"
import CredentialsProvider from "next-auth/providers/credentials"
import axios from "axios"

export default NextAuth({
    providers: [
        CredentialsProvider({
            // 세션 쿠키만 검증하니까 credentials 필드는 비워둬도 됨
            name: "SpringSession",
            credentials: {},
            async authorize(_, req) {
                const res = await axios.get(
                    `${process.env.SPRING_API_BASE}/usr/member/myInfo`,
                    {
                        headers: { cookie: req.headers.cookie },
                        withCredentials: true,
                    }
                )
                if (res.status === 200 && res.data.member) {
                    const m = res.data.member
                    return { id: m.id, nickName: m.nickName, email: m.email }
                }
                return null
            }
        }),
    ],

    session: { strategy: "jwt" },
    jwt:     { secret: process.env.NEXTAUTH_SECRET },

    callbacks: {
        async jwt({ token, user }) {
            if (user) token.user = user
            return token
        },
        async session({ session, token }) {
            session.user = token.user
            return session
        }
    },

    cookies: {
        sessionToken: {
            name: "__Secure-next-auth.session-token",
            options: {
                httpOnly: true,
                sameSite: "none",
                secure: process.env.NODE_ENV === "production",
                path: "/",
            },
        },
    },
})
