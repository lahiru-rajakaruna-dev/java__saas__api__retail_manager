package lahiru_rajakaruna.retail_manager.Common;

import java.util.List;

public interface ISearchable<Entity, SearchParameterDTO> {

    public List<Entity> search(SearchParameterDTO parameters) throws Exception;
}
