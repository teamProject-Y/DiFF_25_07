import Head from 'next/head'

export default function LoginPage() {
    return(
        <>
            <Head>
                <title>로그인</title>
                <meta charSet="UTF-8" />
                <link
                    href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css"
                    rel="stylesheet"
                />
            </Head>
            <div className="flex justify-center items-center min-h-screen bg-gray-100">
                <div className="bg-white p-8 rounded shadow-md w-full max-w-sm">
                    <h2 className="text-2xl font-bold mb-6 text-center">로그인</h2>

                    <a
                        href="/login/github"
                        className="flex items-center justify-center gap-3 bg-black text-white py-2 px-4 rounded hover:bg-gray-800 transition"
                    >
                        <img
                            src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"
                            alt="GitHub Logo"
                            className="w-6 h-6"
                        />
                        <span>GitHub로 로그인</span>
                    </a>

                    <a
                        href="/login/google"
                        className="flex items-center justify-center gap-3 bg-red-500 text-white py-2 px-4 rounded hover:bg-red-600 mt-4"
                    >
                        <img
                            src="https://developers.google.com/identity/images/g-logo.png"
                            alt="Google Logo"
                            className="w-5 h-5"
                        />
                        <span>Google로 로그인</span>
                    </a>
                </div>
            </div>
        </>
    )
}