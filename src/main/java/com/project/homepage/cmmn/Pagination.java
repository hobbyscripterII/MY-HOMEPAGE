package com.project.homepage.cmmn;

public class Pagination {
    private int start; 			// 페이지네이션 시작 번호
    private int end; 			// 페이지네이션 끝 번호
    private int realEnd; 		// 실제 끝 번호
    private boolean prev; 		// 이전 버튼
    private boolean next; 		// 다음 버튼
    private int page; 			// 페이지 번호(1 페이지, 2 페이지...)
    private int amount; 		// 한 페이지당 보여 줄 게시글 개수
    private int total; 			// 총 게시글 개수
    private int pageCnt = 10;	// 페이지네이션 개수

    public Pagination(int page, int amount, int total) {
        this.page 	= page < 0 ? 1 : page;
        this.amount	= amount;
        this.total 	= total;

        // 1. 끝 페이지 계산: (int) Math.ceil(페이지 번호 / 페이지네이션 개수) * 페이지네이션 버튼 개수
        // (1) 1 / 5 = 0.2
        // (2) Math.ceil(0.2) * 5 = 5
        end = (int) Math.ceil(page / (double) pageCnt) * pageCnt;
        // 2. 시작 페이지 계산: 끝 페이지 번호 - 페이지네이션 버튼 개수 + 1
        // (1) 5 - 5 + 1 = 1
        start = end - pageCnt + 1;
        // 3. 실제 끝 번호 계산: (int) Math.ceil(전체 게시글 수 / 데이터 개수)
        // (1) 2 / 10 = 0.2
        // (2) Math.ceil(0.2) = 1
        realEnd = (int) Math.ceil(total / (double) amount);
        // 4. 끝 페이지 다시 계산
        // (1) 5 > 1 ? 1 : 5
        end = end > realEnd ? realEnd : end;
        // 5. 이전 버튼
        // start는 1, 11, 21... 식으로 증가되니 1보다 크면 true를 반환한다.
        prev = start > 1;
        // 6. 다음 버튼
        // next는 realEnd가 end보다 크면 true를 반환한다.
        next = realEnd > end;
    }

	public int getStart() 						{ return start; }
	public void setStart(int start) 			{ this.start = start; }
	public int getEnd() 						{ return end; }
	public void setEnd(int end) 				{ this.end = end; }
	public int getRealEnd() 					{ return realEnd; }
	public void setRealEnd(int realEnd) 		{ this.realEnd = realEnd; }
	public boolean isPrev() 					{ return prev; }
	public void setPrev(boolean prev) 			{ this.prev = prev; }
	public boolean isNext() 					{ return next; }
	public void setNext(boolean next) 			{ this.next = next; }
	public int getPage() 						{ return page; }
	public void setPage(int page) 				{ this.page = page; }
	public int getAmount() 						{ return amount; }
	public void setAmount(int amount) 			{ this.amount = amount; }
	public int getTotal() 						{ return total; }
	public void setTotal(int total) 			{ this.total = total; }
	public int getPageCnt() 					{ return pageCnt; }
	public void setPageCnt(int pageCnt) 		{ this.pageCnt = pageCnt; }

	@Override
	public String toString() {
		return "Pagination [start=" + start + ", end=" + end + ", realEnd=" + realEnd + ", prev=" + prev + ", next=" + next + ", page=" + page + ", amount=" + amount + ", total=" + total + ", pageCnt=" + pageCnt + "]";
	}
}