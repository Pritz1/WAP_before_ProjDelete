package com.eis.wap.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import com.eis.wap.model.Product;

public class FocusProdDaoImpl implements FocusProdDaoCustom{

	@Autowired
	EntityManager entityManager;
	
	public ArrayList<Object[]> getFocProdFromProdMst(String hqRef, String fmsDbRef){
		String sql="select prodid,pname,d1d2 from "+fmsDbRef+".prodmst where d1d2=:hqRef and powerbrand='Y' and deldate='0000-00-00' ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("hqRef", hqRef);
		ArrayList<Object[]> list = (ArrayList<Object[]>)query.getResultList();
		return list;
	}

	@Override
	@Modifying  
	@Transactional
	public int removeAllFocProd(String hqRef, String fmsDbRef) {
		String sql="update "+fmsDbRef+".prodmst set powerbrand='N' where d1d2=:hqRef and powerbrand='Y' ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("hqRef", hqRef);
		return query.executeUpdate();
	}

	@Override
	@Modifying  
	@Transactional
	public int removeSelFocProducts(String hqRef, String fmsDbRef,String prodidArr) {
		String sql="update "+fmsDbRef+".prodmst set powerbrand='N' where d1d2=:hqRef and powerbrand='Y' and prodid in ("+prodidArr+") ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("hqRef", hqRef);
		return query.executeUpdate();
	}

	@Override
	public ArrayList<Object[]> getAllProds(String hqRef, String fmsDbRef) {
		
		String sql="select p.prodid,pname,p.d1d2 from "+fmsDbRef+".prodmst p inner join "
					+fmsDbRef+".prodtrans t on (p.prodid=t.prodid) where p.d1d2=:hqRef and powerbrand='N' and ptype='1' and p.deldate='0000-00-00' ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("hqRef", hqRef);
		ArrayList<Object[]> list = (ArrayList<Object[]>)query.getResultList();
		return list;

	}
	
	@Override
	@Modifying  
	@Transactional
	public int addFocProds(String hqRef, String fmsDbRef,String prodidArr) {
		String sql="update "+fmsDbRef+".prodmst set powerbrand='Y' where d1d2=:hqRef and deldate='0000-00-00' and prodid in ("+prodidArr+") ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("hqRef", hqRef);
		return query.executeUpdate();
	}
	
	
	public ArrayList<Object[]> getCurrLockedFocProd(String loginYr,String loginMth,int divId,String fmsDbRef){
		String sql="select p.prodid,p.pname,p.d1d2 from FocProd f inner join "+fmsDbRef+".prodmst p "
				+ " on (p.prodid=f.prodid) where f.yr=:loginYr and f.mth=:loginMth and f.divid=:divId and p.ptype='1'  ";
	
	Query query = entityManager.createNativeQuery(sql);
	query.setParameter("loginYr", loginYr);
	query.setParameter("loginMth", loginMth);
	query.setParameter("divId", divId);
	ArrayList<Object[]> list = (ArrayList<Object[]>)query.getResultList();
	return list;

	}
	
	public ArrayList<Object[]> getFocProducts(String frmYrMth,String toYrMth,int divId,String fmsDbRef){
		String sql="select yr,mth,p.prodid,p.pname from FocProd f inner join "+fmsDbRef+".prodmst p "
				+ " on (p.prodid=f.prodid) where concat(f.yr,f.mth)>=:frmYrMth and concat(f.yr,f.mth)<=:toYrMth and "
				+ "f.divid=:divId and p.ptype='1'  order by concat(yr,mth),pname ";
	
	Query query = entityManager.createNativeQuery(sql);
	query.setParameter("frmYrMth", frmYrMth);
	query.setParameter("toYrMth", toYrMth);
	query.setParameter("divId", divId);
	ArrayList<Object[]> list = (ArrayList<Object[]>)query.getResultList();
	return list;

	}
	
}
