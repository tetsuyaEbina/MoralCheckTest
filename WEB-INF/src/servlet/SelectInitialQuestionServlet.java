package servlet;

//問題を選択し、次の画面に渡すサーブレット
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.InitialAnswerLog;
import beans.InitialQuestion;
import dao.InitialAnswerLogDAO;
import dao.InitialQuestionDAO;
import irt.EstimateAbility;

public class SelectInitialQuestionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings({ "unchecked", "null" })
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(false);

		//何項目か受け取る
		int countId = Integer.parseInt(request.getParameter("countId"));

		//項目所要時間を計算する
		String dateFrom = (String) session.getAttribute("dateTo");
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
		String dateTo = sdf.format(c.getTime());
		long diff = Long.parseLong(dateTo) - Long.parseLong(dateFrom);
		String answerItemTime = String.valueOf(diff);

		/*
		 * 問題に正答しているかどうかを判別
		 * まず、sessionで保持している、受験者が解いた問題を取り出す
		 */
		InitialQuestion initialQuestion = (InitialQuestion) session.getAttribute("initialQuestion");
		int[] correctAnswerList = { initialQuestion.getCorrect_answer1(), initialQuestion.getCorrect_answer2(),
				initialQuestion.getCorrect_answer3(), initialQuestion.getCorrect_answer4() };
		//次に、実際に回答することによって得られる反応データを取得する
		int answerList[] = new int[4];
		// 選択された番号を1、選択されてない番号を0の配列に直す
		int answer = Integer.parseInt(request.getParameter("answer"));
		for (int i = 0; i < 4; i++) {
			if (i + 1 == answer) {
				answerList[i] = 1;
			} else {
				answerList[i] = 0;
			}
		}

		//correctAnswerListとanswerListの内容が一致しているか確認
		int trueOrFalse;
		boolean judge = Arrays.equals(answerList, correctAnswerList);
		if (judge == false) {
			trueOrFalse = 0;
		} else {
			trueOrFalse = 1;
		}

		ArrayList<Integer> u = null;
		ArrayList<Double> a = null;
		ArrayList<Double> b = null;
		/*
		 * 能力値を算出するために必要な回答履歴をsessionで保持する
		 * 必要な変数は、trueOrFale(u),discrimination(a),difficulty(b)
		 * countIdが1だったときと2以上で場合分け
		 */

		if (countId == 1) {
			u.add(trueOrFalse);
			a.add(initialQuestion.getDiscrimination());
			b.add(initialQuestion.getDifficulty());
			session.setAttribute("u", u);
			session.setAttribute("a", a);
			session.setAttribute("b", b);

		} else if (countId >= 2) {
			u = (ArrayList<Integer>) session.getAttribute("u");
			a = (ArrayList<Double>) session.getAttribute("a");
			b = (ArrayList<Double>) session.getAttribute("b");
			u.add(trueOrFalse);
			a.add(initialQuestion.getDiscrimination());
			b.add(initialQuestion.getDifficulty());
			session.setAttribute("u", u);
			session.setAttribute("a", a);
			session.setAttribute("b", b);
		}

		//能力値を算出する(ベイズのEAP)
		EstimateAbility estimateThetaEAP = new EstimateAbility();
		double thetaEAP = estimateThetaEAP.estimateTheta(u, a, b);

		//オブジェクトの宣言
		InitialAnswerLog initialAnswerLog = new InitialAnswerLog(0, (Integer) session.getAttribute("userId"),
				initialQuestion.getId(), initialQuestion.getDiscrimination(), initialQuestion.getDifficulty(),
				trueOrFalse, thetaEAP, answerList[0], answerList[1], answerList[2], answerList[3], answerItemTime);

		//DBに回答情報を蓄積する
		InitialAnswerLogDAO initialAnswerLogDAO = new InitialAnswerLogDAO();
		initialAnswerLogDAO.insertAnswerLog(initialAnswerLog);

		/*
		 * 次に出す項目をsessionに保持する(上書き)
		 * 項目を更新する
		 */
		InitialQuestionDAO initialQuestionDAO = new InitialQuestionDAO();
		initialQuestion = initialQuestionDAO.selectInitialQuestion((countId + 1));
		session.setAttribute("dateTo", dateTo);
		session.setAttribute("dateFrom", dateFrom);
		session.setAttribute("initialQuestion", initialQuestion);

		request.setAttribute("countId", (countId + 1));

	}
}
