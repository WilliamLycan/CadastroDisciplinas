//******************************************************

// Instituto Federal de São Paulo - Campus Sertãozinho

// Disciplina......: M3LPBD

// Programação de Computadores e Dispositivos Móveis

// Aluno...........: William dos Santos Amaral

//******************************************************

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Vector;

public class Remover extends JFrame {
    private JPanel contentPane;
    private JComboBox comboBox;
    private JTable table;
    private JButton buttonCancel;
    private JButton buttonRemove;
    private Connection con;

    public Remover() {
        con = MySQLConnection.getInstance().getConnection();

        setContentPane(contentPane);
        setTitle("Remover Disciplinas");
        setTable();
        loadData();

        buttonRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRemove();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // Chama a função onCancel quando o 'X' da janela é clicado.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void setTable(){
        DefaultTableModel model = new DefaultTableModel(){
            // Desabilita a edição nas células.
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        model.addColumn("Disciplina");
        model.addColumn("Carga Horária");
        model.addColumn("Curso");
        model.addColumn("Vagas");
        model.addColumn("Período");

        table.setModel(model);
    }

    private void loadData(){
        String cmd = "SELECT * FROM disciplinas";

        try {
           ResultSet res = con.createStatement().executeQuery(cmd);
           System.out.println(cmd);
           while(res.next()){
               Vector v = new Vector();
               v.add(res.getString("nome"));
               v.add(res.getInt("cargaHoraria"));
               v.add(res.getString("curso"));
               v.add(res.getInt("vagas"));
               v.add(res.getString("periodo"));
               ((DefaultTableModel)table.getModel()).addRow(v);
               comboBox.addItem(res.getString("nome"));
           }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void onRemove() {
        if((JOptionPane.showConfirmDialog(contentPane, "Tem certeza de que deseja remover essa disciplina?", "Confirmação", JOptionPane.YES_NO_OPTION)) == 0){
            String cmd = "DELETE FROM disciplinas WHERE nome = '" +
                    comboBox.getSelectedItem() + '\'';

            try {
                con.createStatement().executeUpdate(cmd);
                System.out.println(cmd);
                // Este trecho remove a linha do modelo da tabela, onde o nome da disciplina seja igual ao nome que está selecionado no comboBox.
                String tableValue;
                for(int x = 0; x < ((DefaultTableModel)table.getModel()).getRowCount(); x++){
                    tableValue = (String)((DefaultTableModel)table.getModel()).getValueAt(x, 0);
                    if(tableValue.equals(comboBox.getSelectedItem())){
                        ((DefaultTableModel)table.getModel()).removeRow(x);
                        break;
                    }
                }
                // Remove o nome do comboBox.
                comboBox.removeItem(comboBox.getSelectedItem());
                JOptionPane.showMessageDialog(contentPane, "Dados removidos!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } catch(SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(contentPane, "Erro de conexão. Os dados não foram removidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onCancel() {
        try {
            con.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        dispose();
    }

    public static void main(String[] args) {
        Remover frame = new Remover();
        frame.pack();
        frame.setVisible(true);
        //System.exit(0);
    }
}
