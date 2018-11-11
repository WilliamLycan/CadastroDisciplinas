//******************************************************

// Instituto Federal de São Paulo - Campus Sertãozinho

// Disciplina......: M3LPBD

// Programação de Computadores e Dispositivos Móveis

// Aluno...........: William dos Santos Amaral

//******************************************************

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class Cadastrar extends JFrame {
    private JPanel contentPane;
    private JButton buttonCadastrar;
    private JButton buttonCancelar;
    private JTextField textFieldNome;
    private JTextField textFieldCargaHoraria;
    private JTextField textFieldCurso;
    private JTextField textFieldVagas;
    private JComboBox comboBoxPeriodo;
    private JTextField textFieldPeriodo; // remover
    private Connection con;

    public Cadastrar() {
        con = MySQLConnection.getInstance().getConnection();
        setContentPane(contentPane);
        setComboBox();
        setTitle("Cadastrar Disciplinas");

        buttonCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCadastrar();
            }
        });

        buttonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancelar();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancelar();
            }
        });
    }

    // Verifica se todos os campos estão preenchidos.
    private boolean checkBoxes(){
        if(textFieldNome.getText().isEmpty() ||  textFieldCargaHoraria.getText().isEmpty() || textFieldCurso.getText().isEmpty() || textFieldVagas.getText().isEmpty()){
            return false;
        }
        return true;
    }

    // Verifica se não há letras nos campos numéricos.
    private boolean checkNumbers(){
        String ch = textFieldCargaHoraria.getText();
        String vagas = textFieldVagas.getText();

        for(int x = 0; x < ch.length(); x++){
            if(ch.charAt(x) < '0' || ch.charAt(x) > '9'){
                return false;
            }
        }

        for(int x = 0; x < vagas.length(); x++){
            if(vagas.charAt(x) < '0' || vagas.charAt(x) > '9'){
                return false;
            }
        }

        return true;
    }

    // Limpa as caixas de texto e reseta o comboBox.
    private void clear(){
        textFieldNome.setText("");
        textFieldCargaHoraria.setText("");
        textFieldCurso.setText("");
        textFieldVagas.setText("");
        comboBoxPeriodo.setSelectedIndex(0);
    }

    // Monta o comboBox
    private void setComboBox(){
        comboBoxPeriodo.addItem("Selecione");
        comboBoxPeriodo.addItem("Manhã");
        comboBoxPeriodo.addItem("Tarde");
        comboBoxPeriodo.addItem("Noite");
    }

    // Executado ao clicar no botão cadastrar, faz todas as checagens necessárias antes de enviar o comando ao banco de dados.
    private void onCadastrar(){
        if(checkBoxes() && !((String)comboBoxPeriodo.getSelectedItem()).equals("Selecione")){
            if(checkNumbers()){
                if(JOptionPane.showConfirmDialog(contentPane, "Tem certeza de que quer cadastrar os dados inseridos?", "Confirmação", JOptionPane.YES_NO_OPTION) == 0){

                    String cmd = "INSERT INTO disciplinas (nome, cargaHoraria, curso, vagas, periodo) VALUES (" +
                            '\'' + textFieldNome.getText() + "\', " +
                            textFieldCargaHoraria.getText() + ',' +
                            '\'' + textFieldCurso.getText() + "\', " +
                            textFieldVagas.getText() + ',' +
                            '\'' + comboBoxPeriodo.getSelectedItem() + "\')";
                    try{
                        con.createStatement().executeUpdate(cmd);
                        System.out.println(cmd);
                        JOptionPane.showMessageDialog(contentPane, "Cadastro efetuado!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        clear();
                    } catch (SQLException e){
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(contentPane, "Erro de conexão. Os dados não foram cadastrados.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(contentPane, "Os campos \"Carga Horária\" e \"Vagas\" devem conter apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(contentPane, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Fecha a conexão e termina o programa
    private void onCancelar(){
        try {
            con.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        dispose();
    }

    public static void main(String[] args){
        Cadastrar frame = new Cadastrar();
        frame.pack();
        frame.setVisible(true);
        //System.exit(0);
    }
}
