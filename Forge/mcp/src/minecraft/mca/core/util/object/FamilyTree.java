/*******************************************************************************
 * FamilyTree.java
 * Copyright (c) 2013 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/

package mca.core.util.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mca.core.MCA;
import mca.core.io.WorldPropertiesManager;
import mca.entity.AbstractEntity;
import mca.enums.EnumRelation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Handles information about each person's family.
 */
public class FamilyTree implements Serializable
{
	/** The actual owner of this family tree.*/
	public transient AbstractEntity owner;

	/** Map containing the IDs of entities related to the owner as the key, and their relation to the owner as the value.*/
	private Map<Integer, EnumRelation> relationMap;

	/**
	 * Constructor
	 * 
	 * @param	entity	The owner of the family tree.
	 */
	public FamilyTree(AbstractEntity entity)
	{
		owner = entity;
		relationMap = new HashMap<Integer, EnumRelation>();
	}

	/**
	 * Gets the opposing relation of the specified relation value.
	 * For instance, if someone is your MOTHER, then you are their SON or DAUGHTER.
	 * 
	 * @param	gender		The gender of the entity who should have the opposing relation that is returned.	
	 * @param 	relation	The relation to find the opposing relation of.
	 * 
	 * @return	EnumRelation constant that is the opposing relation of the specified relation value.
	 */
	public static EnumRelation getOpposingRelation(String gender, EnumRelation relation)
	{
		switch (relation)
		{
			case Aunt:
				if (gender.equals("Male")) return EnumRelation.Nephew; else return EnumRelation.Niece;
			case Brother:
				if (gender.equals("Male")) return EnumRelation.Brother; else return EnumRelation.Sister;
			case Cousin:
				return EnumRelation.Cousin;
			case Daughter:
				if (gender.equals("Male")) return EnumRelation.Father; else return EnumRelation.Mother;
			case Father:
				if (gender.equals("Male")) return EnumRelation.Son; else return EnumRelation.Daughter;
			case Granddaughter:
				if (gender.equals("Male")) return EnumRelation.Grandfather; else return EnumRelation.Grandmother;
			case Grandson:
				if (gender.equals("Male")) return EnumRelation.Grandfather; else return EnumRelation.Grandmother;
			case Greatgranddaughter:
				if (gender.equals("Male")) return EnumRelation.Greatgrandfather; else return EnumRelation.Greatgrandmother;
			case Greatgrandson:
				if (gender.equals("Male")) return EnumRelation.Greatgrandfather; else return EnumRelation.Greatgrandmother;
			case Husband:
				if (gender.equals("Male")) return EnumRelation.Husband; else return EnumRelation.Wife;
			case Mother:
				if (gender.equals("Male")) return EnumRelation.Son; else return EnumRelation.Daughter;
			case Nephew:
				if (gender.equals("Male")) return EnumRelation.Uncle; else return EnumRelation.Aunt;
			case Niece:
				if (gender.equals("Male")) return EnumRelation.Uncle; else return EnumRelation.Aunt;
			case Sister:
				if (gender.equals("Male")) return EnumRelation.Brother; else return EnumRelation.Sister;
			case Son:
				if (gender.equals("Male")) return EnumRelation.Father; else return EnumRelation.Mother;
			case Uncle:
				if (gender.equals("Male")) return EnumRelation.Nephew; else return EnumRelation.Niece;
			case Wife:
				if (gender.equals("Male")) return EnumRelation.Husband; else return EnumRelation.Wife;
			case Spouse:
				if (gender.equals("Male")) return EnumRelation.Husband; else return EnumRelation.Wife;
			case Grandparent:
				if (gender.equals("Male")) return EnumRelation.Grandson; else return EnumRelation.Granddaughter;
			case Greatgrandparent:
				if (gender.equals("Male")) return EnumRelation.Greatgrandson; else return EnumRelation.Greatgranddaughter;
			case Parent:
				if (gender.equals("Male")) return EnumRelation.Son; else return EnumRelation.Daughter;
			default:
				break;
		}

		return EnumRelation.None;
	}

	/**
	 * Adds the specified entity and relation value to the family tree.
	 * 
	 * @param 	player		The player being added to the family tree.
	 * @param 	relation	The relation to the owner of the family tree.
	 */
	public void addFamilyTreeEntry(EntityPlayer player, EnumRelation relation)
	{
		//Add the player's ID and relation to the map.
		relationMap.put(MCA.instance.getIdOfPlayer(player), relation);
	}

	/**
	 * Adds the specified entity and relation value to the family tree.
	 * 
	 * @param 	entity		The entity being added to the family tree.
	 * @param 	relation	The entity's relation to the owner of the family tree.
	 */
	public void addFamilyTreeEntry(AbstractEntity entity, EnumRelation relation)
	{
		//Add the entity's ID and relation to the map.
		if (entity != null)
		{
			relationMap.put(entity.mcaID, relation);
		}
	}

	/**
	 * Adds the specified int and relation value to the family tree.
	 * 
	 * @param 	id			The ID to add to the family tree.
	 * @param 	relation	The relation of the entity with the specified ID to the owner of the family tree.
	 */
	public void addFamilyTreeEntry(int id, EnumRelation relation)
	{
		//Add the ID and relation to the map.
		relationMap.put(id, relation);
	}

	/**
	 * Removes the provided player from the family tree.
	 * 
	 * @param 	player	The player to remove from the family tree.
	 */
	public void removeFamilyTreeEntry(EntityPlayer player)
	{
		relationMap.remove(MCA.instance.getIdOfPlayer(player));
	}
	
	/**
	 * Removes the provided entity from the family tree.
	 * 
	 * @param 	entity	The entity to remove from the family tree.
	 */
	public void removeFamilyTreeEntry(AbstractEntity entity)
	{
		relationMap.remove(entity.mcaID);
	}
	
	/**
	 * Removes the provided ID from the family tree.
	 * 
	 * @param 	mcaId	The ID to remove from the family tree.
	 */
	public void removeFamilyTreeEntry(int mcaId)
	{
		relationMap.remove(mcaId);
	}
	
	/**
	 * Removes the provided EnumRelation from the family tree.
	 * 
	 * @param 	relation	The EnumRelation to remove from the family tree.
	 */
	public void removeFamilyTreeEntry(EnumRelation relation)
	{
		int removalKey = 0;
		
		for(Map.Entry<Integer, EnumRelation> entry : relationMap.entrySet())
		{
			if (entry.getValue().equals(relation))
			{
				removalKey = entry.getKey();
			}
		}
		
		relationMap.remove(removalKey);
	}
	
	/**
	 * Checks if an entity is related to the owner of this family tree.
	 * 
	 * @param	entity	The entity being checked for relation to the owner.
	 * 
	 * @return	boolean identifying whether or not the provided entity is related to the owner of the family tree.
	 */
	public boolean entityIsRelative(AbstractEntity entity)
	{
		//Easily self explanatory.
		if (relationMap.containsKey(entity.mcaID))
		{
			return true;
		}

		else
		{
			return false;
		}
	}

	/**
	 * Checks if an id is related to the owner of this family tree.
	 * 
	 * @param	id	The entity id being checked for relation to the owner.
	 * 
	 * @return	boolean identifying whether or not the provided entity is related to the owner of the family tree.
	 */
	public boolean idIsRelative(int id)
	{
		//Easily self explanatory.
		if (relationMap.containsKey(id))
		{
			return true;
		}

		else
		{
			return false;
		}
	}

	/**
	 * Gets a person's relation to the owner from the map.
	 * 
	 * @param 	entity	The entity whose relationship is being retrieved.
	 * 
	 * @return	The relation of the entity provided to the owner of this family tree.
	 */
	public EnumRelation getRelationOf(AbstractEntity entity)
	{
		if (entityIsRelative(entity))
		{
			return relationMap.get(entity.mcaID);
		}

		else
		{
			return EnumRelation.None;
		}
	}

	/**
	 * Gets a person's relation to the owner from the map.
	 * 
	 * @param 	id	The ID of the entity.
	 * 
	 * @return	The relation of the entity with the provided ID.
	 */
	public EnumRelation getRelationOf(int id)
	{
		if (idIsRelative(id))
		{
			EnumRelation returnRelation = relationMap.get(id);
			
			if (returnRelation.equals(EnumRelation.Greatgrandparent))
			{
				if (id < 0)
				{
					WorldPropertiesManager manager = MCA.instance.playerWorldManagerMap.get(MCA.instance.getPlayerByID(owner.worldObj, id).username);
					
					if (manager.worldProperties.playerGender.equals("Male"))
					{
						return EnumRelation.Greatgrandfather;
					}
					
					else
					{
						return EnumRelation.Greatgrandmother;
					}
				}
			}
			
			else if (returnRelation.equals(EnumRelation.Grandparent))
			{
				if (id < 0)
				{
					WorldPropertiesManager manager = MCA.instance.playerWorldManagerMap.get(MCA.instance.getPlayerByID(owner.worldObj, id).username);
					
					if (manager.worldProperties.playerGender.equals("Male"))
					{
						return EnumRelation.Grandfather;
					}
					
					else
					{
						return EnumRelation.Grandmother;
					}
				}
			}
			
			return relationMap.get(id);
		}

		else
		{
			return EnumRelation.None;
		}
	}
	
	/**
	 * Gets a person's relation to the owner from the map.
	 * 
	 * @param 	entity	The entity whose relationship is being retrieved.
	 * 
	 * @return	The relation of the entity provided to the owner of this family tree.
	 */
	public EnumRelation getRelationTo(AbstractEntity entity)
	{
		if (idIsRelative(entity.mcaID))
		{
			return getOpposingRelation(owner.gender, relationMap.get(entity.mcaID));
		}

		else
		{
			return EnumRelation.None;
		}
	}

	/**
	 * Gets a person's relation to the owner from the map.
	 * 
	 * @param 	id	The ID of the entity.
	 * 
	 * @return	The relation of the entity with the provided ID.
	 */
	public EnumRelation getRelationTo(int id)
	{
		if (idIsRelative(id))
		{
			return getOpposingRelation(owner.gender, relationMap.get(id));
		}

		else
		{
			return EnumRelation.None;
		}
	}

	/**
	 * Gets the ID of the first entity contained in the family tree that has the specified relation.
	 * 
	 * @param	relation	The EnumRelation value that an entity should have.
	 * 
	 * @return	The entity's ID who has the specified relation to the owner of the family tree. 0 if one is not found.
	 */
	public int getEntityWithRelation(EnumRelation relation)
	{
		for (Map.Entry<Integer, EnumRelation> entry : relationMap.entrySet())
		{
			if (entry.getValue() == relation)
			{
				return entry.getKey();
			}
		}
		
		return 0;
	}

	/**
	 * Get a list of all the entities contained in the family tree that have the specified relation.
	 * 
	 * @param	relation	The EnumRelation value that an entity should have.
	 * 
	 * @return	List containing the IDs of entities who have the specified relation to the owner of the family tree.
	 */
	public List<Integer> getEntitiesWithRelation(EnumRelation relation)
	{
		List<Integer> returnList = new ArrayList<Integer>();

		for (Map.Entry<Integer, EnumRelation> entry : relationMap.entrySet())
		{
			if (entry.getValue() == relation)
			{
				returnList.add(entry.getKey());
			}
		}

		return returnList;
	}

	/**
	 * Writes the entity's family tree to NBT.
	 * 
	 * @param	NBT	The NBT object that saves information about the entity.
	 */
	public void writeTreeToNBT(NBTTagCompound NBT)
	{
		int counter = 0;

		for(Map.Entry<Integer, EnumRelation> KVP : relationMap.entrySet())
		{
			NBT.setInteger("familyTreeEntryID" + counter, KVP.getKey());
			NBT.setString("familyTreeEntryRelation" + counter, KVP.getValue().getValue());

			counter++;
		}
	}

	/**
	 * Reads the entity's family tree from NBT.
	 * 
	 * @param	NBT	The NBT object that reads information about the entity.
	 */
	public void readTreeFromNBT(NBTTagCompound NBT)
	{
		int counter = 0;

		while (true)
		{
			try
			{
				int entryID = NBT.getInteger("familyTreeEntryID" + counter);
				String entryRelation = NBT.getString("familyTreeEntryRelation" + counter);

				if (entryID == 0 && entryRelation.equals(""))
				{
					break;
				}

				else
				{
					relationMap.put(entryID, EnumRelation.getEnum(entryRelation));
					counter++;
				}
			}

			catch (NullPointerException e)
			{
				break;
			}
		}
	}

	/**
	 * Writes all information about this family tree to the console.
	 */
	public void dumpTreeContents()
	{
		MCA.instance.log("Family tree of " + owner.name + ". MCA ID: " + owner.mcaID);

		for (Map.Entry<Integer, EnumRelation> entry : relationMap.entrySet())
		{
			MCA.instance.log(entry.getKey() + " : " + entry.getValue().getValue());
		}
	}

	/**
	 * Gets an instance of the entity whose relation to this entity matches the provided relation.
	 * 
	 * @param 	relation	The relation of the entity that should be returned.
	 * 
	 * @return	Entity whose relation to this entity matches the provided relation.
	 */
	public AbstractEntity getInstanceOfRelative(EnumRelation relation) 
	{
		for (Map.Entry<Integer, EnumRelation> entrySet : relationMap.entrySet())
		{
			for (Object obj : owner.worldObj.loadedEntityList)
			{
				if (obj instanceof AbstractEntity)
				{
					AbstractEntity entity = (AbstractEntity)obj;

					if (entity.mcaID == entrySet.getKey() && entity.familyTree.getRelationOf(owner) == relation)
					{
						return entity;
					}
				}
			}
		}

		return null;
	}
	
	/**
	 * Gets a list of all the players related to this entity.
	 * 
	 * @return	A list of all the players contained in the relation map.
	 */
	public List<Integer> getListOfPlayers()
	{
		List<Integer> returnList = new ArrayList<Integer>();
		
		for (Integer integer : relationMap.keySet())
		{
			//All player IDs are negative.
			if (integer < 0 && !returnList.contains(integer))
			{
				returnList.add(integer);
			}
		}
		
		return returnList;
	}
	
	/**
	 * Sets the relation map of the family tree to the provided map value.
	 * 
	 * @param 	map	The map containing relation information.
	 */
	public void setRelationMap(Map<Integer, EnumRelation> map)
	{
		this.relationMap = map;
	}
	
	/**
	 * Returns a clone of this family tree.
	 * 
	 * @return	Value copy of the family tree referenced by this instance of FamilyTree.
	 */
	public FamilyTree clone()
	{
		FamilyTree returnTree = new FamilyTree(owner);
		returnTree.setRelationMap(relationMap);
		return returnTree;
	}
}
