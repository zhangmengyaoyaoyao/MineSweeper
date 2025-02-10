import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.*;
public class MineSweeper implements ActionListener {
    JFrame frame=new JFrame("扫雷游戏");
    JButton reset=new JButton("重来");
    Container container=new Container();

    //游戏数据结构
    final int row=20;
    final int col=20;
    final int bombCount =30;
    JButton [][] buttons=new JButton[row][col];
    int [][] counts=new int[row][col];
    final int BOMBCODE =10;

    // 构造函数
    public MineSweeper(){
        //1、设置窗口
        frame.setSize(900, 900);
        frame.setResizable(true);//是否可改变窗口大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        //2、添加重来按钮
        addResetButton();
        //添加按钮
        addButtons();

        //埋雷
        addBomb();
        //添加雷的计算
        calcNeiboLei();
        frame.setVisible(true);
    }
    public void addResetButton(){
        reset.setBackground(Color.WHITE);
        reset.setOpaque(true);
        reset.addActionListener(this);
        frame.add(reset,BorderLayout.NORTH);
    }

    public void addBomb(){
        Random rand=new Random();
        int randRow,randCol;
        for(int i = 0; i< bombCount; i++){
            randRow=rand.nextInt(row);
            randCol=rand.nextInt(col);
            if(counts[randRow][randCol]== BOMBCODE){
                i--;
            }else{
                counts[randRow][randCol]= BOMBCODE;
                //buttons[randRow][randCol].setText("*");

            }
        }
    }
    public void addButtons(){
        frame.add(container,BorderLayout.CENTER);
        container.setLayout(new GridLayout(row,col));
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                JButton button=new JButton();
                button.setBackground(Color.yellow);
                button.setOpaque(true);
                button.addActionListener(this);
                buttons[i][j]=button;
                container.add(button);
            }
        }
    }
    public void calcNeiboLei(){
        int count;
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                count=0;
                if(counts[i][j]== BOMBCODE) continue;

                if(i>0 && j>0 && counts[i-1][j-1]== BOMBCODE) count++;
                if(i>0&&counts[i-1][j]== BOMBCODE) count++;
                if(i>0 && j<19 && counts[i-1][j+1]== BOMBCODE) count++;
                if(j>0 && counts[i][j-1]== BOMBCODE) count++;
                if(j<19 && counts[i][j+1]== BOMBCODE) count++;
                if(i<19&&j>0&&counts[i+1][j-1]== BOMBCODE) count++;
                if(i<19&&counts[i+1][j]== BOMBCODE) count++;
                if(i<19&&j<19&&counts[i+1][j+1]== BOMBCODE) count++;

                counts[i][j]=count;
                //buttons[i][j].setText(counts[i][j]+"");
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        JButton button=(JButton)e.getSource();
        if(button.equals(reset)){
            for(int i=0;i<row;i++){
                for(int j=0;j<col;j++){
                    buttons[i][j].setText("");
                    buttons[i][j].setEnabled(true);
                    buttons[i][j].setBackground(Color.yellow);
                    counts[i][j]=0;
                }
            }
            addBomb();
            calcNeiboLei();
        }else{
            int count=0;
            for(int i=0;i<row;i++){
                for(int j=0;j<col;j++){
                    if(button.equals(buttons[i][j])){
                        count=counts[i][j];
                        if(count== BOMBCODE){
                            LoseGame();
                        }else{
                            openCell(i,j);
                            checkWin();
                        } return;
                    }
                }
            }


        }
    }
    void checkWin(){
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                if(buttons[i][j].isEnabled()==true && counts[i][j]!= BOMBCODE) return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Yeah,你赢了！");
    }
    void openCell(int i,int j){
        if(buttons[i][j].isEnabled()==false) return;

        buttons[i][j].setEnabled(false);
        if(counts[i][j]==0){
            if(i>0 && j>0 && counts[i-1][j-1]!= BOMBCODE) openCell(i-1, j-1);
            if(i>0&&counts[i-1][j]!= BOMBCODE) openCell(i-1, j);
            if(i>0 && j<19 && counts[i-1][j+1]!= BOMBCODE) openCell(i-1, j+1);
            if(j>0 && counts[i][j-1]!= BOMBCODE) openCell(i, j-1);
            if(j<19 && counts[i][j+1]!= BOMBCODE) openCell(i, j+1);
            if(i<19&&j>0&&counts[i+1][j-1]!= BOMBCODE) openCell(i+1, j-1);
            if(i<19&&counts[i+1][j]!= BOMBCODE) openCell(i+1, j);
            if(i<19&&j<19&&counts[i+1][j+1]!= BOMBCODE) openCell(i+1, j+1);

            buttons[i][j].setText(counts[i][j]+"");
        }else{
            buttons[i][j].setText(counts[i][j]+"");
        }
    }

    void LoseGame(){
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                int count=counts[i][j];
                if(count== BOMBCODE){
                    buttons[i][j].setText("X");
                    buttons[i][j].setBackground(Color.red);
                    buttons[i][j].setEnabled(false);
                }else{
                    buttons[i][j].setText(count+"");
                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }
    public static void main(String[] args) {
        MineSweeper mineSweeper = new MineSweeper();
    }

}