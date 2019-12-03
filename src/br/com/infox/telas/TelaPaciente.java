package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import javax.swing.JOptionPane;
// alinha abaixo importa recursos da biblioteca rs2xml.jat
import net.proteanit.sql.DbUtils;

public class TelaPaciente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaPaciente() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

// metodo para adicionar paciente
    private void adicionar() {
        String sql = "insert into tbpacient (nome, ende, fone, idade, email, sexo, nasci) values(?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPNome.getText());
            pst.setString(2, txtPEndereco.getText());
            pst.setString(3, txtPTelefone.getText());
            pst.setString(4, txtPIdade.getText());
            pst.setString(5, txtPEmail.getText());
            pst.setString(6, cbPSexo.getSelectedItem().toString());
            pst.setString(7, txtNascimento.getText().toString());
            //validação dos campos obrigatorios
            if ((txtPNome.getText().isEmpty()) || (txtPTelefone.getText().isEmpty()) || (txtPEndereco.getText().isEmpty()) || (txtPIdade.getText().isEmpty()) || (txtNascimento.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os campos obrigatórios!");
            } else {
                //a linha abaixo atualiza a tabela paciente com os dados do formuláeio
                //a estrutura abaixo é usada para confirmar que o usuario foi adicionado
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Paciente cadastrado com sucesso!");
                    txtPNome.setText(null);
                    txtPEndereco.setText(null);
                    txtPTelefone.setText(null);
                    txtPEmail.setText(null);
                    txtPIdade.setText(null);
                    txtNascimento.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para pesquisar clientes pelo nome do filtro
    private void pesquisar_paciente() {
        String sql = "select * from tbpacient where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteudo da caixa de pesquisa para o ?
            //atenção ao % que é a continuação da String sql
            pst.setString(1, txtPPesquisar.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblPacientes.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
        }
    }

    //metodo para setar os campos do formulário com o conteúdo da tabela
    public void setar_campos() {
        int setar = tblPacientes.getSelectedRow();
        txtPId.setText(tblPacientes.getModel().getValueAt(setar, 0).toString());
        txtPNome.setText(tblPacientes.getModel().getValueAt(setar, 1).toString());
        txtPEndereco.setText(tblPacientes.getModel().getValueAt(setar, 2).toString());
        txtPTelefone.setText(tblPacientes.getModel().getValueAt(setar, 3).toString());
        txtPIdade.setText(tblPacientes.getModel().getValueAt(setar, 4).toString());
        txtPEmail.setText(tblPacientes.getModel().getValueAt(setar, 5).toString());
        cbPSexo.setSelectedItem(tblPacientes.getModel().getValueAt(setar, 6).toString());
        txtNascimento.setText(tblPacientes.getModel().getValueAt(setar, 7).toString());
        btnPAdicionar.setEnabled(false);
    }

    //metodo para alterar dados dos pacientes
    private void alterar() {
        String sql = "update tbpacient set nome=?, ende=?, fone=?, idade=?, email=?, nasci=?, sexo=? where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPNome.getText());
            pst.setString(2, txtPEndereco.getText());
            pst.setString(3, txtPTelefone.getText());
            pst.setString(4, txtPIdade.getText());
            pst.setString(5, txtPEmail.getText());
            pst.setString(6, txtNascimento.getText().toString());
            pst.setString(7, cbPSexo.getSelectedItem().toString());
            pst.setString(8, txtPId.getText());
            if ((txtPNome.getText().isEmpty()) || (txtPTelefone.getText().isEmpty()) || (txtPEndereco.getText().isEmpty()) || (txtPIdade.getText().isEmpty()) || (txtNascimento.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os campos obrigatórios!");
            } else {
                //a linha abaixo atualiza a tabela paciente com os dados do formuláeio
                //a estrutura abaixo é usada para confirmar que o usuario foi alterado
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do paciente alterados com sucesso!");
                    txtPNome.setText(null);
                    txtPEndereco.setText(null);
                    txtPTelefone.setText(null);
                    txtPEmail.setText(null);
                    txtPIdade.setText(null);
                    txtNascimento.setText(null);
                    btnPAdicionar.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo responsavel pela remoção de paciente
    private void remover() {
        //a estrutura abaixo confirma a remoção do paciente
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esse paciente?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbpacient where id=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtPId.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Paciente removido com sucesso!");
                    txtPNome.setText(null);
                    txtPEndereco.setText(null);
                    txtPTelefone.setText(null);
                    txtPEmail.setText(null);
                    txtPIdade.setText(null);
                    txtNascimento.setText(null);
                    btnPAdicionar.setEnabled(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField5 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPNome = new javax.swing.JTextField();
        txtPEndereco = new javax.swing.JTextField();
        txtPTelefone = new javax.swing.JTextField();
        txtPEmail = new javax.swing.JTextField();
        cbPSexo = new javax.swing.JComboBox<>();
        btnPAdicionar = new javax.swing.JButton();
        btnPAlterar = new javax.swing.JButton();
        btnPRemover = new javax.swing.JButton();
        txtPPesquisar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPacientes = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtPId = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPIdade = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtNascimento = new javax.swing.JTextField();

        jTextField5.setText("jTextField5");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de Paciente");
        setPreferredSize(new java.awt.Dimension(1015, 565));

        jLabel1.setText("Nome*:");

        jLabel2.setText("Endereço*:");

        jLabel3.setText("Telefone*:");

        jLabel4.setText("E-mail:");

        jLabel5.setText("Sexo*:");

        cbPSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));

        btnPAdicionar.setText("Adicionar");
        btnPAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPAdicionarActionPerformed(evt);
            }
        });

        btnPAlterar.setText("Alterar");
        btnPAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPAlterarActionPerformed(evt);
            }
        });

        btnPRemover.setText("Remover");
        btnPRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPRemoverActionPerformed(evt);
            }
        });

        txtPPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPPesquisarKeyReleased(evt);
            }
        });

        tblPacientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblPacientes.getTableHeader().setReorderingAllowed(false);
        tblPacientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPacientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPacientes);

        jLabel7.setText("ID:");

        txtPId.setEnabled(false);

        jLabel8.setText("Idade*:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setText("Cadastrar novo Paciente");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Procurar por paciente");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("* Campos Obrigatórios");

        jLabel9.setText("Pesquisar:");

        jLabel10.setText("Data de Nascimento*:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jLabel20)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPNome, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(txtPIdade, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbPSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtPEndereco)
                                    .addComponent(txtPEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtPId, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(36, 36, 36)
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(btnPAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnPRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addGap(112, 112, 112)))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnPAdicionar, btnPAlterar, btnPRemover});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtPId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(txtNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtPNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtPIdade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(cbPSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtPEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtPTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPAlterar)
                            .addComponent(btnPRemover)
                            .addComponent(btnPAdicionar)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(174, 174, 174))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnPAdicionar, btnPAlterar, btnPRemover});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPAdicionarActionPerformed
        //chamando o metodo adicionar
        adicionar();
    }//GEN-LAST:event_btnPAdicionarActionPerformed

    //o evento abaixo é do tipo "enquanto for digitando"
    private void txtPPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPPesquisarKeyReleased
        // chamar método pesquisar clientes
        pesquisar_paciente();
    }//GEN-LAST:event_txtPPesquisarKeyReleased

    //evento que será usado para setar os campos da tabela (clicando com o mouse)
    private void tblPacientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPacientesMouseClicked
        //chamando o metodo para setar os campos
        setar_campos();
    }//GEN-LAST:event_tblPacientesMouseClicked

    //cjhamando o metodo para alterar paciente
    private void btnPAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPAlterarActionPerformed
        alterar();
    }//GEN-LAST:event_btnPAlterarActionPerformed

    private void btnPRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPRemoverActionPerformed
        // chamando o metodo para remover um paciente
        remover();
    }//GEN-LAST:event_btnPRemoverActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPAdicionar;
    private javax.swing.JButton btnPAlterar;
    private javax.swing.JButton btnPRemover;
    private javax.swing.JComboBox<String> cbPSexo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTable tblPacientes;
    private javax.swing.JTextField txtNascimento;
    private javax.swing.JTextField txtPEmail;
    private javax.swing.JTextField txtPEndereco;
    private javax.swing.JTextField txtPId;
    private javax.swing.JTextField txtPIdade;
    private javax.swing.JTextField txtPNome;
    private javax.swing.JTextField txtPPesquisar;
    private javax.swing.JTextField txtPTelefone;
    // End of variables declaration//GEN-END:variables
}
