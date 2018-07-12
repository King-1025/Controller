package king.helper.model;

public class InstructionLine
{
	public Instruction[] quequeelem;
	public int front;
	public int rear;
	public int maxsize;
	
	//初始化
	public InstructionLine(int n)
	{
		front = 0;
		rear = 0;
		maxsize = n;
		quequeelem = new Instruction[n];
	}
	
	//清空队列
	public void clear()
	{
		rear = front = 0;
	}
	//判断队列是否为空
	public boolean isEmpty()
	{
		if(rear == front)
			return true;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
		return false;
	}
	//返回队列长度
	public int length()
	{
		return (rear-front+maxsize)%maxsize;
	}
	//将数据元素x插入到队列中成为队尾元素
	//入队
	public void insert(Instruction x) throws Exception
	{
		if((rear+1)%maxsize == front)
		{
			throw new Exception("the queue is full!");
		}
		quequeelem[rear] = x;
		rear = (rear+1)%maxsize;
	}
	//出队
	public Object poll() throws Exception
	{
		if(rear == front)
			throw new Exception("the queue is empty!");
		Object xObject = quequeelem[front];
		front = (front + 1)%maxsize; 
		return xObject;
	}
	public void print()
	{
		if(!isEmpty())
			for(int i=front;i<rear;i=(i+1)%maxsize)
			{
				System.out.print(quequeelem[i]+" ");
			}
		else {
			System.out.println("the queue is empty!");
		}
		System.out.println();
	}
	
}
