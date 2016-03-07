package com.rogueai.snmp2bean.codegen.model.builder;

import java.util.ArrayList;
import java.util.List;

import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.snmp.SnmpIndex;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.type.IntegerType;
import net.percederberg.mibble.type.StringType;
import net.percederberg.mibble.value.ObjectIdentifierValue;

import com.rogueai.snmp2bean.codegen.model.MibContainerNode;
import com.rogueai.snmp2bean.codegen.model.MibValueNode;

/**
 * 
 * @author zugnom
 * 
 */
public class GenModelBuilder {

	private List<MibContainerNode> parents = new ArrayList<MibContainerNode>();

	public List<MibContainerNode> build(MibValueSymbol root) {
		processMibSymbol(root, null);
		return parents;
	}

	private void processMibSymbol(MibValueSymbol symbol, MibContainerNode parent) {
		if (symbol.isTable()) {
			processMibSymbol(symbol.getChildren()[0], parent);
		}
		if (symbol.getChildren() != null && symbol.getChildren().length > 0) {
			processMibParent(symbol);
		} else {
			processMibNode(symbol, parent);
		}
	}

	private void processMibNode(MibValueSymbol symbol, MibContainerNode parent) {
		MibValueNode node = new MibValueNode();
		node.setName(symbol.getName());

		String oid = createOid(symbol, parent);
		node.setOid(oid.toString());

		processMibType(symbol, node);
		if (parent != null) {
			parent.getChildren().add(node);
		}
	}

	private String createOid(MibValueSymbol symbol, MibContainerNode parent) {
		StringBuilder oid = new StringBuilder();
		oid.append(".");
		oid.append(symbol.getValue().toString());
		if (parent != null) {
			oid.append(parent.isTable() ? "" : ".0");
		}
		return oid.toString();
	}

	private void processMibType(MibValueSymbol symbol, MibValueNode node) {
		if (symbol.getType() instanceof SnmpObjectType) {
			SnmpObjectType type = (SnmpObjectType) symbol.getType();
			String description = type.getDescription().replaceAll("(\\r|\\n)", " ");
			node.setDescription(description);
			node.setJavaType(getJavaType(type));
			node.setSmiType(getSmiType(type));
		}
	}

	private List<MibValueSymbol> processMibIndex(MibValueSymbol symbol, MibContainerNode parent) {
		ArrayList<MibValueSymbol> indexSymbols = new ArrayList<MibValueSymbol>();
		if (symbol.isTableRow()) {
			SnmpObjectType type = (SnmpObjectType) symbol.getType();
			for (Object o : type.getIndex()) {
				if (o instanceof SnmpIndex) {
					SnmpIndex index = (SnmpIndex) o;
					ObjectIdentifierValue value = (ObjectIdentifierValue) index.getValue();

					MibValueNode indexNode = new MibValueNode();
					indexNode.setName(value.getName());

					String indexOid = "." + index.toString();
					indexNode.setIndex(true);

					indexNode.setOid(indexOid);

					processMibType(value.getSymbol(), indexNode);
					parent.getChildren().add(indexNode);

					indexSymbols.add(value.getSymbol());
				}
			}
		}
		return indexSymbols;
	}

	private void processMibParent(MibValueSymbol symbol) {
		MibContainerNode node = new MibContainerNode();
		node.setTable(symbol.isTableRow());
		node.setName(symbol.getName());
		node.setOid(createOid(symbol, null));

		// process indexes: we collect their referenced Symbols so we avoid
		// generating them twice if they happen to be primary keys (e.g.:
		// children) for the table
		List<MibValueSymbol> indexSymbols = processMibIndex(symbol, node);

		MibValueSymbol[] mibChildren = symbol.getChildren();
		for (MibValueSymbol mibChild : mibChildren) {
			if (!indexSymbols.contains(mibChild)) {
				processMibSymbol(mibChild, node);
			}
		}
		if (!node.getChildren().isEmpty()) {
			parents.add(node);
		}
	}

	private String getJavaType(SnmpObjectType mibType) {
		MibType syntax = mibType.getSyntax();
		if (syntax instanceof StringType) {
			return "String";
		} else if (syntax instanceof IntegerType) {
			if (syntax.hasReferenceTo("CounterBasedGauge64")) {
				return "Long";
			} else if (syntax.hasReferenceTo("Counter64")) {
				return "Long";
			} else if (syntax.hasReferenceTo("Integer32")) {
				return "Integer";
			} else if (syntax.hasReferenceTo("Gauge32")) {
				return "Integer";
			} else if (syntax.hasReferenceTo("Counter32")) {
				return "Integer";
			}
			return "Integer";
		}
		return ""; // we want to generate a compile error
	}

	private String getSmiType(SnmpObjectType mibType) {
		MibType syntax = mibType.getSyntax();
		if (syntax instanceof StringType) {
			return "DISPLAY_STRING";
		} else if (syntax instanceof IntegerType) {
			if (syntax.hasReferenceTo("CounterBasedGauge64")) {
				return "COUNTER64";
			} else if (syntax.hasReferenceTo("Counter64")) {
				return "COUNTER64";
			} else if (syntax.hasReferenceTo("Integer32")) {
				return "INTEGER";
			} else if (syntax.hasReferenceTo("Gauge32")) {
				return "INTEGER";
			} else if (syntax.hasReferenceTo("Counter32")) {
				return "INTEGER";
			}
			return "INTEGER";
		}
		return ""; // we want to generate a compile error
	}

}
