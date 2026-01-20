import {execFile} from "child_process";
import {promisify} from "util";

const execFileAsync = promisify(execFile);

async function runHurl(hurlFile) {
    try {
        const res = await execFileAsync('hurl', ['--json', hurlFile]);
        const outputRaw = res.stdout;
        const output = JSON.parse(outputRaw);
        console.log("RECEIVED", output);
        return output.success;
    } catch (error) {
        console.error('Hurl failed:', error.stderr);
        throw error;
    }
}

const val = await runHurl("index.hurl")

export const handler = async (event) => {
    const greenUrl = event.hookDetails?.test_url || process.env.GREEN_URL;
}
