const { Command } = require('commander');
const simpleGit = require('simple-git');
const fs = require('fs');
const path = require('path');
const program = new Command();

const git = simpleGit();

program
    .name('diff-cli')
    .description('Git 커밋 분석 CLI')
    .version('1.0.0');

program
    .command('analyze')
    .description('최근 커밋 분석')
    .option('-s, --summary <text>', '수동 요약 추가')
    .action(async (options) => {
        console.log('🧪 Git 분석 시작...');

        try {
            const log = await git.log({ n: 1 });
            const latestCommit = log.latest;

            const diff = await git.diff([`${latestCommit.hash}^!`]);

            const summary = options.summary || '[자동 요약 없음]';

            console.log('\n📝 최근 커밋 메시지:\n', latestCommit.message);
            console.log('\n🔄 변경된 코드:\n', diff);
            console.log('\n✏️ 요약:\n', summary);

            // Markdown 저장
            const md = `# ${latestCommit.message}\n\n## 요약\n${summary}\n\n## 코드 변경사항\n\`\`\`diff\n${diff}\n\`\`\`\n`;
            const fileName = `blog-${Date.now()}.md`;
            fs.writeFileSync(path.join(__dirname, fileName), md);
            console.log(`\n✅ Markdown 저장됨: ${fileName}`);

        } catch (err) {
            console.error('❌ Git 분석 중 오류 발생:', err);
        }
    });

program.parse();
