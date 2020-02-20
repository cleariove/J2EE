package tmall.util;

public class Page
{
    //项目的编号从0开始
    private int start;//起始位置
    private int count;//页大小
    private int total;//数据总数
    private String param;

    public Page()
    {
    }

    public Page(int start, int count)
    {
        this.start = start;
        this.count = count;
    }

    public int getTotalPage()
    {
        int pageNumber = total / count;
        if(total % count != 0)
            pageNumber += 1;
        return pageNumber > 0 ? pageNumber : 1;
    }

    public boolean isHasPrevious()
    {
        return start != 0;
    }

    public boolean isHasNext()
    {
        return start != getLast();
    }

    public int getLast()
    //获得最后一页第一项的号码
    {
        int last;
        // 假设总数是50，是能够被5整除的，那么最后一页的开始就是45
        if (0 == total % count)
            last = total - count;
            // 假设总数是51，不能够被5整除的，那么最后一页的开始就是50
        else
            last = total - total % count;

        last = last<0?0:last;
        return last;
    }

    public int getStart()
    {
        return start;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public String getParam()
    {
        return param;
    }

    public void setParam(String param)
    {
        this.param = param;
    }
}
