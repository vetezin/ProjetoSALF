package projetoSalf.mvc.model;

import java.time.LocalDate;

public class NecessidadePC {
    private int npcCod;
    private String npcDesc;
    private LocalDate npcDtcriacao;
    private String npcStatus;
    private int funcCod;
    private int pcCod;

    public NecessidadePC() {
    }

    public NecessidadePC(int npcCod, String npcDesc, LocalDate npcDtcriacao, String npcStatus, int funcCod, int pcCod) {
        this.npcCod = npcCod;
        this.npcDesc = npcDesc;
        this.npcDtcriacao = npcDtcriacao;
        this.npcStatus = npcStatus;
        this.funcCod = funcCod;
        this.pcCod = pcCod;
    }

    public int getNpcCod() {
        return npcCod;
    }

    public void setNpcCod(int npcCod) {
        this.npcCod = npcCod;
    }

    public String getNpcDesc() {
        return npcDesc;
    }

    public void setNpcDesc(String npcDesc) {
        this.npcDesc = npcDesc;
    }

    public LocalDate getNpcDtcriacao() {
        return npcDtcriacao;
    }

    public void setNpcDtcriacao(LocalDate npcDtcriacao) {
        this.npcDtcriacao = npcDtcriacao;
    }

    public String getNpcStatus() {
        return npcStatus;
    }

    public void setNpcStatus(String npcStatus) {
        this.npcStatus = npcStatus;
    }

    public int getFuncCod() {
        return funcCod;
    }

    public void setFuncCod(int funcCod) {
        this.funcCod = funcCod;
    }

    public int getPcCod() {
        return pcCod;
    }

    public void setPcCod(int pcCod) {
        this.pcCod = pcCod;
    }
}

